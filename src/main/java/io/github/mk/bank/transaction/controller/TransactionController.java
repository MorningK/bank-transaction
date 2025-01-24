package io.github.mk.bank.transaction.controller;

import io.github.mk.bank.transaction.model.Transaction;
import io.github.mk.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  public Transaction create(@RequestBody TransactionService.CreateTransactionRequest data) {
    return transactionService.create(data);
  }
}
