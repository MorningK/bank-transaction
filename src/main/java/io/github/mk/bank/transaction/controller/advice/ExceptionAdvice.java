package io.github.mk.bank.transaction.controller.advice;

import io.github.mk.bank.transaction.exception.BackendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler
  public Response<?> handleValidationException(MethodArgumentNotValidException exception) {
    return new Response<>(
        BackendException.ErrorCode.BAD_REQUEST.getCode(), null, exception.getMessage());
  }
}
