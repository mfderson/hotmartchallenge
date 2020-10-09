package com.hotmartchalenge.marketplace.domain.exceptions;

public class DateFormatException extends ParameterFormatException {
  private static final long serialVersionUID = 1L;

  public DateFormatException(String message) {
    super(message);
  }
}
