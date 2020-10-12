package com.hotmartchalenge.marketplace.utils;

import com.hotmartchalenge.marketplace.domain.exceptions.DateFormatException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FormatDatetimeUtils {

  private static final String MSG_FORMAT_DATE = "The date must be in the format YYYY-MM-DD";

  public static OffsetDateTime convertTimeToStartDay(String dateToBeConverted) {
    try {
      return OffsetDateTime.parse(
          String.format("%sT00:00:00+00:00", dateToBeConverted.substring(0, 10)),
          DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    } catch (StringIndexOutOfBoundsException e) {
      throw new DateFormatException(MSG_FORMAT_DATE);
    }
  }

  public static OffsetDateTime convertTimeToEndDay(String dateToBeConverted) {
    try {
      return OffsetDateTime.parse(
          String.format("%sT23:59:59+00:00", dateToBeConverted.substring(0, 10)),
          DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    } catch (StringIndexOutOfBoundsException e) {
      throw new DateFormatException(MSG_FORMAT_DATE);
    }
  }

  public static OffsetDateTime convertTimeToStartOfDay(OffsetDateTime dateToBeConverted) {
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

  public static OffsetDateTime convertTimeToFinalOfDay(OffsetDateTime dateToBeConverted) {
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
