package io.github.mk.bank.transaction.controller;

import io.github.mk.bank.transaction.exception.BackendException;
import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/transactions")
@RestController
public class TransactionController {
  private final TransactionService transactionService;

  @GetMapping("/")
  public Page<Transaction> transactions(Pageable pageable) {
    return transactionService.transactions(pageable);
  }

  @PostMapping("/")
  public Transaction create(@Valid @RequestBody TransactionService.CreateTransactionRequest data) {
    return transactionService.create(data);
  }

  @GetMapping("/{id}")
  public Transaction transaction(@PathVariable @Positive Long id) throws BackendException {
    return transactionService.findById(id);
  }

  @PutMapping("/{id}")
  public Transaction update(
      @PathVariable @Positive Long id,
      @Valid @RequestBody TransactionService.UpdateTransactionRequest data)
      throws BackendException {
    return transactionService.update(id, data);
  }

  @DeleteMapping("/{id}")
  public boolean delete(@PathVariable @Positive Long id) throws BackendException {
    transactionService.delete(id);
    return true;
  }
}
