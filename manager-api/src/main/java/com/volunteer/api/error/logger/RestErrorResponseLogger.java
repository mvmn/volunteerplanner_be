package com.volunteer.api.error.logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.text.TextStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class RestErrorResponseLogger extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(RestErrorResponseLogger.class);

  private static final List<MediaType> TEXT_CONTENT_TYPES = Arrays.asList(
      MediaType.valueOf("text/*"),
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.valueOf("application/*+json"),
      MediaType.valueOf("application/*+xml")
  );

  private static final List<MediaType> FORM_CONTENT_TYPES = Arrays.asList(
      MediaType.APPLICATION_FORM_URLENCODED,
      MediaType.MULTIPART_FORM_DATA
  );

  @Override
  protected void doFilterInternal(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain filterChain)
      throws ServletException, IOException {
    if (isAsyncDispatch(request)) {
      filterChain.doFilter(request, response);
    } else {
      doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
    }
  }

  private void doFilterWrapped(final ContentCachingRequestWrapper request,
      final ContentCachingResponseWrapper response,
      final FilterChain filterChain) throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } finally {
      try {
        logErrorResponse(request, response);
      } catch (final Exception ex) {
        LOG.error("Unable to log the response", ex);
      }
      response.copyBodyToResponse();
    }
  }

  private void logErrorResponse(final ContentCachingRequestWrapper request,
      final ContentCachingResponseWrapper response) {
    final int status = response.getStatus();
    if ((status >= HttpStatus.OK.value()) && (status < HttpStatus.BAD_REQUEST.value())) {
      return;
    }

    final TextStringBuilder builder = new TextStringBuilder();

    processRequestHeader(request, builder, "---");
    processRequestBody(request, builder, ">>>");
    processResponse(response, builder, "<<<");

    LOG.error(builder.toString());
  }

  private void processRequestHeader(final ContentCachingRequestWrapper request,
      final TextStringBuilder builder, final String prefix) {
    final String queryString = request.getQueryString();
    if (queryString == null) {
      builder.appendln("%s %s %s", prefix, request.getMethod(), request.getRequestURI());
    } else {
      builder.appendln("%s %s %s?%s", prefix, request.getMethod(), request.getRequestURI(),
          queryString);
    }

    Collections.list(request.getHeaderNames()).forEach(headerName ->
        Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
            builder.appendln("%s %s: %s", prefix, headerName, headerValue)));
  }

  private void processRequestBody(final ContentCachingRequestWrapper request,
      final TextStringBuilder builder, final String prefix) {
    final Map<String, String[]> params = request.getParameterMap();
    processParameters(params, request.getContentType(), builder, prefix);

    final byte[] content = request.getContentAsByteArray();
    processContent(content, request.getContentType(), request.getCharacterEncoding(), builder,
        prefix);
  }

  private void processResponse(final ContentCachingResponseWrapper response,
      final TextStringBuilder builder,
      final String prefix) {
    builder.appendln(prefix);

    final int status = response.getStatus();
    builder.appendln("%s Status: %d %s", prefix, status,
        HttpStatus.valueOf(status).getReasonPhrase());

    final byte[] content = response.getContentAsByteArray();
    processContent(content, response.getContentType(), response.getCharacterEncoding(), builder,
        prefix);
  }

  private void processContent(final byte[] content, final String contentType,
      final String contentEncoding,
      final TextStringBuilder builder, final String prefix) {
    if (Objects.isNull(content) || content.length == 0 || StringUtils.hasText(contentType)) {
      return;
    }

    final MediaType mediaType = MediaType.valueOf(contentType);
    final boolean isTextContent = TEXT_CONTENT_TYPES.stream().anyMatch(
        visibleType -> visibleType.includes(mediaType));

    if (!isTextContent) {
      return;
    }

    builder.appendln(prefix);
    try {
      final String contentString = new String(content, contentEncoding);
      Stream.of(contentString.split("\r\n|\r|\n"))
          .forEach(line -> builder.appendln("%s %s", prefix, line));
    } catch (final UnsupportedEncodingException ex) {
      builder.appendln("%s [%d bytes content]", prefix, content.length);
    }
  }

  private void processParameters(final Map<String, String[]> parameters, final String contentType,
      final TextStringBuilder builder, final String prefix) {
    if (Objects.isNull(parameters) || parameters.isEmpty() || StringUtils.hasText(contentType)) {
      return;
    }

    final MediaType mediaType = MediaType.valueOf(contentType);
    final boolean isParametersContent = FORM_CONTENT_TYPES.stream().anyMatch(
        visibleType -> visibleType.includes(mediaType));

    if (!isParametersContent) {
      return;
    }

    builder.appendln(prefix);
    parameters.forEach((parameterName, parameterValues) ->
        builder.appendln("%s %s: %s", prefix, parameterName,
            String.join(";", parameterValues))
    );
  }

  private ContentCachingRequestWrapper wrapRequest(final HttpServletRequest request) {
    if (request instanceof ContentCachingRequestWrapper) {
      return (ContentCachingRequestWrapper) request;
    } else {
      return new ContentCachingRequestWrapper(request);
    }
  }

  private ContentCachingResponseWrapper wrapResponse(final HttpServletResponse response) {
    if (response instanceof ContentCachingResponseWrapper) {
      return (ContentCachingResponseWrapper) response;
    } else {
      return new ContentCachingResponseWrapper(response);
    }
  }

}
