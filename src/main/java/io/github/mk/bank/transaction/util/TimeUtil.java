package io.github.mk.bank.transaction.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("+08");
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

  private TimeUtil() {}

  public static String formatCurrentTimestamp() {
    OffsetDateTime now = OffsetDateTime.now(DEFAULT_ZONE_ID);
    return now.format(DATE_TIME_FORMATTER);
  }
}
