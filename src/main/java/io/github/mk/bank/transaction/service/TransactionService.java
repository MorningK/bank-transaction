package io.github.mk.bank.transaction.service;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface TransactionService {
  Page<Transaction> transactions(Pageable pageable);

  Transaction create(CreateTransactionRequest data);

  Transaction findById(Long id) throws BackendException;

  Transaction update(Long id, UpdateTransactionRequest data) throws BackendException;

  void delete(Long id) throws BackendException;

  record CreateTransactionRequest(
      @NotNull @NotBlank @Pattern(regexp = "\\d+") String account,
      @NotNull @Positive Integer amount,
      @Nullable String remark) {}

  record UpdateTransactionRequest(@Nullable String remark) {}
}
