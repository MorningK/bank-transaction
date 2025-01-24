package io.github.mk.bank.transaction.config;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;

@Configuration
public class JpaConfig {
  private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("+08");

  @Bean
  public DateTimeProvider offsetDateTimeProvider() {
    return () -> Optional.of(OffsetDateTime.now(DEFAULT_ZONE_ID));
  }
}
