package com.volunteer.api.config;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.volunteer.api.data.model.api.ErrorResponse;
import com.volunteer.api.error.InvalidPasswordException;
import com.volunteer.api.error.InvalidQuantityException;
import com.volunteer.api.error.InvalidStatusException;
import com.volunteer.api.error.ObjectNotFoundException;
import com.volunteer.api.error.SmsServiceCommunicationException;
import com.volunteer.api.error.TurboSmsSendFailureException;

@RestControllerAdvice
@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
public class ErrorHandler {

  private static final Map<String, String> CONSTRAINS_I18N_MAP = new HashMap<>();

  static {
    CONSTRAINS_I18N_MAP.put("un_user_display_name", "user.displayName");
    CONSTRAINS_I18N_MAP.put("un_user_phone_number", "user.phoneNumber");
    CONSTRAINS_I18N_MAP.put("un_category", "category.name");
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
    return ErrorResponse.builder()
        .errorMessage(exception.getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", ")))
        .build();
  }

  @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handle(Exception exception) {
    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }

  @ExceptionHandler({InvalidPasswordException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handle(InvalidPasswordException exception) {
    return ErrorResponse.builder().errorMessage("Old password does not match current one").build();
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public ErrorResponse handleException(DataIntegrityViolationException exception) {
    if (exception.getCause() instanceof ConstraintViolationException) {
      return handleException((ConstraintViolationException) exception.getCause());
    }

    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public ErrorResponse handleException(final ConstraintViolationException exception) {
    final String mappedName = CONSTRAINS_I18N_MAP.get(exception.getConstraintName());
    if (StringUtils.isNotEmpty(mappedName)) {
      return ErrorResponse.builder().errorMessage(mappedName + " is not unique").build();
    }

    return ErrorResponse.builder().errorMessage(exception.getConstraintName() + " violated")
        .build();
  }

  @ExceptionHandler({ObjectNotFoundException.class, EntityNotFoundException.class,
      EmptyResultDataAccessException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorResponse handleException(Exception exception) {
    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }

  @ExceptionHandler({InvalidStatusException.class, InvalidQuantityException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBadRequestException(Exception exception) {
    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }

  @ExceptionHandler(TurboSmsSendFailureException.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleTurboSmsSendFailureException(TurboSmsSendFailureException exception) {
    return ErrorResponse.builder().errorMessage("SMS sending failed").build();
  }

  @ExceptionHandler(SmsServiceCommunicationException.class)
  @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
  public ErrorResponse handleSmsServiceCommunicationException(
      SmsServiceCommunicationException exception) {
    return ErrorResponse.builder().errorMessage("SMS sending service communication failure")
        .build();
  }
}
