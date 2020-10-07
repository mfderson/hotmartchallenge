package com.hotmartchalenge.marketplace.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FormatDatetimeUtils {

  public static OffsetDateTime convertTimeToStartDay(String dateToBeConverted) {
    return OffsetDateTime.parse(
        String.format("%sT00:00:00+00:00", dateToBeConverted),
        DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static OffsetDateTime convertTimeToEndDay(String dateToBeConverted) {
    return OffsetDateTime.parse(
        String.format("%sT23:59:59+00:00", dateToBeConverted),
        DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static OffsetDateTime convertTimeToStartDay(OffsetDateTime dateToBeConverted) {
    return OffsetDateTime.of(
        dateToBeConverted.getYear(),
        dateToBeConverted.getMonthValue(),
        dateToBeConverted.getDayOfMonth(),
        0,
        0,
        0,
        0,
        ZoneOffset.UTC);
  }

  public static OffsetDateTime convertTimeToEndDay(OffsetDateTime dateToBeConverted) {
    return OffsetDateTime.of(
        dateToBeConverted.getYear(),
        dateToBeConverted.getMonthValue(),
        dateToBeConverted.getDayOfMonth(),
        23,
        59,
        59,
        0,
        ZoneOffset.UTC);
  }

  public static boolean compareTwoDatesOnlyDatePart(OffsetDateTime date1, OffsetDateTime date2) {
    if (date1.getDayOfMonth() == date2.getDayOfMonth()
        && date1.getMonth() == date2.getMonth()
        && date1.getYear() == date2.getYear()) {
      return true;
    }
    return false;
  }
}
