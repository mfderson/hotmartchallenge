package com.hotmartchalenge.marketplace.domain.exceptions;

public abstract class ParameterFormatException extends BusinessException {
  private static final long serialVersionUID = 1L;

  public ParameterFormatException(String message) {
    super(message);
  }
}
