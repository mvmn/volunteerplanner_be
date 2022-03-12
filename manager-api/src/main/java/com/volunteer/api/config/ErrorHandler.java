package com.volunteer.api.config;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.volunteer.api.data.user.model.api.ErrorResponse;
import com.volunteer.api.error.ObjectNotFoundException;

@RestControllerAdvice
public class ErrorHandler {
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
  public ErrorResponse handleIllegalStateException(IllegalStateException exception) {
    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }


  @ExceptionHandler(ObjectNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorResponse handleException(Exception exception) {
    return ErrorResponse.builder().errorMessage(exception.getMessage()).build();
  }
}
