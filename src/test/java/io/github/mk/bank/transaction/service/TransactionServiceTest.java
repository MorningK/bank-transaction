package io.github.mk.bank.transaction.service;

import static org.junit.jupiter.api.Assertions.*;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import java.util.List;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class TransactionServiceTest {
  @Autowired TransactionService transactionService;

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

  @Test
  @Transactional
  void paginationTest() {
    TransactionService.CreateTransactionRequest createTransactionRequest =
        new TransactionService.CreateTransactionRequest("1234567890", 100, "remark");
    for (int i = 0; i < 100; i++) {
      transactionService.create(createTransactionRequest);
    }
    Pageable pageable = PageRequest.of(0, 10);
    Page<Transaction> transactions = transactionService.transactions(pageable);
    assertEquals(100, transactions.getTotalElements());
    assertEquals(10, transactions.getTotalPages());
    assertEquals(10, transactions.getContent().size());
    assertTrue(transactions.isFirst());
    assertFalse(transactions.isLast());
    assertTrue(transactions.hasNext());
    for (int i = 1; i < 9; i++) {
      pageable = PageRequest.of(i, 10);
      transactions = transactionService.transactions(pageable);
      assertEquals(100, transactions.getTotalElements());
      assertEquals(10, transactions.getTotalPages());
      assertEquals(10, transactions.getContent().size());
      assertFalse(transactions.isFirst());
      assertFalse(transactions.isLast());
      assertTrue(transactions.hasNext());
    }
    pageable = PageRequest.of(9, 10);
    transactions = transactionService.transactions(pageable);
    assertEquals(100, transactions.getTotalElements());
    assertEquals(10, transactions.getTotalPages());
    assertEquals(10, transactions.getContent().size());
    assertFalse(transactions.isFirst());
    assertTrue(transactions.isLast());
    assertFalse(transactions.hasNext());
  }
}
