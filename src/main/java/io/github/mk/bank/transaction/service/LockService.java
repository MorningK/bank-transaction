package io.github.mk.bank.transaction.service;

import java.util.concurrent.locks.Lock;

public interface LockService {
  Lock getLock(String name);
}
