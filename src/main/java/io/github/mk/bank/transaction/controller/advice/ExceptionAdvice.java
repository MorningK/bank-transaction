package io.github.mk.bank.transaction.controller.advice;

import io.github.mk.bank.transaction.exception.BackendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
  @ExceptionHandler
  public Response<?> handleValidationException(MethodArgumentNotValidException exception) {
    String aggregatedErrors = exception.getBindingResult().getFieldErrors()
      .stream()
      .map(err -> err.getField() + " " + err.getDefaultMessage())
      .collect(Collectors.joining("; "));
    return new Response<>(BackendException.ErrorCode.BAD_REQUEST.getCode(), null, aggregatedErrors);
  }
}
