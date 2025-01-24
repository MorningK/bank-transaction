package io.github.mk.bank.transaction.service;

import io.github.mk.bank.transaction.model.Transaction;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
  Page<Transaction> transactions(Pageable pageable);

  Transaction create(CreateTransactionRequest data);

  record CreateTransactionRequest(
      @NotNull @NotBlank @Pattern(regexp = "\\d+") String account,
      @NotNull @Positive Integer amount,
      @Nullable String remark) {}
}
