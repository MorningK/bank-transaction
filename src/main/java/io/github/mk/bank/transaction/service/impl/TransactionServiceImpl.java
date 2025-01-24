package io.github.mk.bank.transaction.service.impl;

import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.repository.TransactionRepository;
import io.github.mk.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
  private final TransactionRepository transactionRepository;

  @Override
  public Page<Transaction> transactions(Pageable pageable) {
    return transactionRepository.findAll(pageable);
  }
}
