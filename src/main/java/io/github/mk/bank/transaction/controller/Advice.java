package io.github.mk.bank.transaction.controller;

import io.github.mk.bank.transaction.exception.BackendException;
import jakarta.annotation.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Advice {
  public record Response<T>(String code, @Nullable T data, @Nullable String message) {
    public static Response<?> of(BackendException exception) {
      return new Response<>(exception.getCode(), null, exception.getMessage());
    }

    public static Response<?> of(RuntimeException exception) {
      return new Response<>(
          BackendException.ErrorCode.INTERNAL_SERVER_ERROR.getCode(), null, exception.getMessage());
    }
  }

  @ExceptionHandler
  public Response<?> handleValidationException(MethodArgumentNotValidException exception) {
    return new Response<>(
        BackendException.ErrorCode.BAD_REQUEST.getCode(), null, exception.getMessage());
  }
}
