package com.volunteer.api.audit;

import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.volunteer.api.service.AuthService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuditInterceptor.class);

  private final AuthService authService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String queryString = request.getQueryString();
    String path =
        request.getRequestURI() + (StringUtils.isNotBlank(queryString) ? "?" + queryString : "");
    MDC.put("REQUEST_ID", UUID.randomUUID().toString());
    MDC.put("REQUEST_ADDRESS", request.getRemoteAddr());
    MDC.put("REQUEST_METHOD", request.getMethod());
    MDC.put("REQUEST_PATH", path);
    MDC.put("REQUEST_USER_ID", authService.getCurrentUserName());
    MDC.put("REQUEST_USER_ROLES",
        authService.getCurrentUserRoles().stream().collect(Collectors.joining(",")));
    LOGGER.info("AUDIT_BEGIN");
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) throws Exception {
    LOGGER.info("AUDIT_END");
  }
}
