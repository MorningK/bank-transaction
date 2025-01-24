package io.github.mk.bank.transaction.service;

import io.github.mk.bank.transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
  Page<Transaction> transactions(Pageable pageable);
}
