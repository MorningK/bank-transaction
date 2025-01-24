package io.github.mk.bank.transaction.controller.advice;

import io.github.mk.bank.transaction.exception.BackendException;
import jakarta.annotation.Nullable;

public record Response<T>(String code, @Nullable T data, @Nullable String message) {
  public static final String SUCCESS_CODE = "200";

  public static Response<?> of(BackendException exception) {
    return new Response<>(exception.getCode(), null, exception.getMessage());
  }

  public static Response<?> of(RuntimeException exception) {
    return new Response<>(
        BackendException.ErrorCode.INTERNAL_SERVER_ERROR.getCode(), null, exception.getMessage());
  }
}
