package io.github.mk.bank.transaction.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BackendException {
  private final String code;
  private final String message;

  public BackendException(ErrorCode errorCode, String message) {
    this(errorCode.getCode(), message);
  }

  @Getter
  @AllArgsConstructor
  public enum ErrorCode {
    NOT_FOUND("404"),
    BAD_REQUEST("400"),
    INTERNAL_SERVER_ERROR("500"),
    ;

    private final String code;
  }
}
