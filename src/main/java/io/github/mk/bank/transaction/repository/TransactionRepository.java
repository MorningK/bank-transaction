package io.github.mk.bank.transaction.repository;

import io.github.mk.bank.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
