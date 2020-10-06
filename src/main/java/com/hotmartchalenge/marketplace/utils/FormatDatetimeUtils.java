package com.hotmartchalenge.marketplace.utils;

import java.time.OffsetDateTime;
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
}
