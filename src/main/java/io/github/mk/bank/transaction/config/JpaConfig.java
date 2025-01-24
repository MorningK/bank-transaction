package io.github.mk.bank.transaction.config;

import static io.github.mk.bank.transaction.util.TimeUtil.DEFAULT_ZONE_ID;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

@Configuration
public class JpaConfig {
  @Bean
  public DateTimeProvider offsetDateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now(DEFAULT_ZONE_ID));
  }
}
