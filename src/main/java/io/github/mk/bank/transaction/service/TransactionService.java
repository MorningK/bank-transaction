package io.github.mk.bank.transaction.service;

import io.github.mk.bank.transaction.model.Transaction;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
  Page<Transaction> transactions(Pageable pageable);

  Transaction create(CreateTransactionRequest data);

  record CreateTransactionRequest(String account, Integer amount, @Nullable String remark) {}
}
