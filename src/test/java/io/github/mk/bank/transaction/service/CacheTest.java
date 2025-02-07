package io.github.mk.bank.transaction.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class CacheTest {
  @Autowired TransactionService transactionService;
  @MockitoBean TransactionRepository transactionRepository;

  @Test
  @Transactional
  void cacheTest() {
    AtomicLong id = new AtomicLong(1);
    Mockito.when(transactionRepository.findAll(Pageable.unpaged())).thenReturn(Page.empty());
    Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
        .then(
            invocation -> {
              Transaction result = invocation.getArgument(0, Transaction.class);
              if (result.getId() != null) {
                return result;
              }
              result.setId(id.getAndIncrement());
              return result;
            });

    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());
    Mockito.verify(transactionRepository, Mockito.times(1)).findAll(Pageable.unpaged());
    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());
    Mockito.verify(transactionRepository, Mockito.times(1)).findAll(Pageable.unpaged());

    TransactionService.CreateTransactionRequest createTransactionRequest =
        new TransactionService.CreateTransactionRequest("1234567890", 100, "remark");
    Transaction transaction = transactionService.create(createTransactionRequest);
    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());
    Mockito.verify(transactionRepository, Mockito.times(2)).findAll(Pageable.unpaged());
    Mockito.when(transactionRepository.findById(transaction.getId()))
        .thenReturn(Optional.of(transaction));
    assertEquals(
        transaction, assertDoesNotThrow(() -> transactionService.findById(transaction.getId())));
    Mockito.verify(transactionRepository, Mockito.times(0)).findById(transaction.getId());

    Transaction updated =
        assertDoesNotThrow(
            () ->
                transactionService.update(
                    transaction.getId(),
                    new TransactionService.UpdateTransactionRequest("remark2")));
    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());
    Mockito.verify(transactionRepository, Mockito.times(3)).findAll(Pageable.unpaged());
    Mockito.when(transactionRepository.findById(updated.getId())).thenReturn(Optional.of(updated));
    assertEquals(updated, assertDoesNotThrow(() -> transactionService.findById(updated.getId())));
    Mockito.verify(transactionRepository, Mockito.times(0)).findById(updated.getId());

    assertDoesNotThrow(() -> transactionService.delete(updated.getId()));
    assertTrue(transactionService.transactions(Pageable.unpaged()).isEmpty());
    Mockito.verify(transactionRepository, Mockito.times(4)).findAll(Pageable.unpaged());
    Mockito.when(transactionRepository.findById(updated.getId())).thenReturn(Optional.empty());
    assertThrows(BackendException.class, () -> transactionService.findById(updated.getId()));
    Mockito.verify(transactionRepository, Mockito.times(1)).findById(updated.getId());
  }
}
