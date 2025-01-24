package io.github.mk.bank.transaction.service;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class TransactionServiceTest {
  @Autowired TransactionService transactionService;
  @Autowired
  TransactionRepository transactionRepository;
  Executor executor = Executors.newFixedThreadPool(10);

  @Order(1)
  @Test
  @Transactional
  void crudTest() {
    Page<Transaction> transactions = transactionService.transactions(Pageable.unpaged());
    assertTrue(transactions.isEmpty());

    TransactionService.CreateTransactionRequest createTransactionRequest =
        new TransactionService.CreateTransactionRequest("1234567890", 100, "remark");
    Transaction transaction = transactionService.create(createTransactionRequest);
    checkCreateResult(transaction, createTransactionRequest);

    assertDoesNotThrow(
        () ->
            transactionService.update(
                transaction.getId(), new TransactionService.UpdateTransactionRequest("remark2")));
    Transaction updated =
        assertDoesNotThrow(() -> transactionService.findById(transaction.getId()));
    assertEquals("remark2", updated.getRemark());

    assertDoesNotThrow(() -> transactionService.delete(transaction.getId()));
    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());

    assertThrows(BackendException.class, () -> transactionService.findById(transaction.getId()));
    assertThrows(
        BackendException.class,
        () ->
            transactionService.update(
                transaction.getId(), new TransactionService.UpdateTransactionRequest("remark3")));
    assertThrows(BackendException.class, () -> transactionService.delete(transaction.getId()));
  }

  private void checkCreateResult(
      Transaction transaction,
      TransactionService.CreateTransactionRequest createTransactionRequest) {
    Page<Transaction> transactions = transactionService.transactions(Pageable.unpaged());
    assertFalse(transactions.isEmpty());
    assertEquals(1, transactions.getTotalElements());
    List<Transaction> content = transactions.getContent();
    assertEquals(1, content.size());
    Transaction first = content.getFirst();
    assertEquals(transaction.getCode(), first.getCode());
    assertEquals(createTransactionRequest.account(), first.getAccount());
    assertEquals(createTransactionRequest.amount(), first.getAmount());
    assertEquals(createTransactionRequest.remark(), first.getRemark());
  }

  @Order(2)
  @Test
  @Transactional
  void paginationTest() {
    TransactionService.CreateTransactionRequest createTransactionRequest =
        new TransactionService.CreateTransactionRequest("1234567890", 100, "remark");
    for (int i = 0; i < 100; i++) {
      transactionService.create(createTransactionRequest);
    }
    checkPaginationResult(100);
  }

  private void checkPaginationResult(int total) {
    int totalPages = Math.ceilDiv(total, 10);
    List<Transaction> all = new ArrayList<>(total);
    Pageable pageable = PageRequest.of(0, 10);
    Page<Transaction> transactions = transactionService.transactions(pageable);

    assertEquals(total, transactions.getTotalElements());
    assertEquals(totalPages, transactions.getTotalPages());
    assertEquals(10, transactions.getContent().size());
    assertTrue(transactions.isFirst());
    assertFalse(transactions.isLast());
    assertTrue(transactions.hasNext());
    all.addAll(transactions.getContent());

    for (int i = 1; i < totalPages - 1; i++) {
      pageable = PageRequest.of(i, 10);
      transactions = transactionService.transactions(pageable);
      assertFalse(transactions.isFirst());
      assertFalse(transactions.isLast());
      assertTrue(transactions.hasNext());
      all.addAll(transactions.getContent());
    }

    pageable = PageRequest.of(totalPages - 1, 10);
    transactions = transactionService.transactions(pageable);
    assertEquals(total, transactions.getTotalElements());
    assertEquals(10, transactions.getContent().size());
    assertFalse(transactions.isFirst());
    assertTrue(transactions.isLast());
    assertFalse(transactions.hasNext());
    all.addAll(transactions.getContent());

    assertEquals(total, all.size());
    assertEquals(total, all.stream().map(Transaction::getCode).distinct().count());
    transactionRepository.deleteAll(all);
  }

  @Order(3)
  @Test
  @Transactional
  void stressTest() {
    TransactionService.CreateTransactionRequest createTransactionRequest =
        new TransactionService.CreateTransactionRequest("1234567890", 100, "remark");
    int total = 1000;
    CountDownLatch countDownLatch = new CountDownLatch(total);
    for (int i = 0; i < total; i++) {
      executor.execute(
          () -> {
            transactionService.create(createTransactionRequest);
            countDownLatch.countDown();
          });
    }
    assertDoesNotThrow(() -> countDownLatch.await());
    checkPaginationResult(total);
  }
}
