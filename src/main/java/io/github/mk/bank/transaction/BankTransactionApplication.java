package io.github.mk.bank.transaction;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableJpaAuditing(dateTimeProviderRef = "offsetDateTimeProvider")
public class BankTransactionApplication {
  public static void main(String[] args) {
    SpringApplication.run(BankTransactionApplication.class, args);
  }
}
