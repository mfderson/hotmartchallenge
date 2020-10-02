package com.hotmartchalenge.marketplace.domain.exceptions;

public class ProductNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = 1L;

  public ProductNotFoundException(String message) {
    super(message);
  }

  public ProductNotFoundException(Long id) {
    this(String.format("There is no product with id %d", id));
  }
}
