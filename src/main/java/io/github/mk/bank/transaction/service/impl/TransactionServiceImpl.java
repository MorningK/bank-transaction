package io.github.mk.bank.transaction.service.impl;

import static io.github.mk.bank.transaction.util.TimeUtil.formatCurrentTimestamp;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.repository.TransactionRepository;
import io.github.mk.bank.transaction.service.LockService;
import io.github.mk.bank.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {
  private static final Random RANDOM = new Random();

  private final TransactionRepository transactionRepository;
  private final LockService lockService;

  @Override
  public Page<Transaction> transactions(Pageable pageable) {
    return transactionRepository.findAll(pageable);
  }

  @Override
  public Transaction create(CreateTransactionRequest data) {
    Transaction transaction =
        Transaction.builder()
            .code(generateCode())
            .account(data.account())
            .amount(data.amount())
            .remark(data.remark())
            .build();
    return transactionRepository.save(transaction);
  }

  @Override
  public Transaction findById(Long id) throws BackendException {
    return transactionRepository
        .findById(id)
        .orElseThrow(
            () ->
                new BackendException(
                    BackendException.ErrorCode.NOT_FOUND, "transaction not found"));
  }

  @Override
  public Transaction update(Long id, UpdateTransactionRequest data) throws BackendException {
    Transaction transaction = findById(id);
    transaction.setRemark(data.remark());
    transaction = transactionRepository.save(transaction);
    return transaction;
  }

  @Transactional
  @Override
  public void delete(Long id) throws BackendException {
    Transaction transaction = findById(id);
    transactionRepository.delete(transaction);
  }

  private String generateCode() {
    Lock lock = lockService.getLock("generateTransactionCode");
    try {
      lock.lock();
      StringBuilder code = new StringBuilder(formatCurrentTimestamp());
      for (int i = 0; i < 4; i++) {
        code.append(RANDOM.nextInt(10));
      }
      return code.toString();
    } finally {
      lock.unlock();
    }
  }
}
