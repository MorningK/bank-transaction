package io.github.mk.bank.transaction.service.impl;

import io.github.mk.bank.transaction.service.LockService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

@Service
public class LockServiceImpl implements LockService {
  private static final Map<String, Lock> lockByName = new ConcurrentHashMap<>();

  @Override
  public Lock getLock(String name) {
    return lockByName.computeIfAbsent(name, (key) -> new ReentrantLock(true));
  }
}
