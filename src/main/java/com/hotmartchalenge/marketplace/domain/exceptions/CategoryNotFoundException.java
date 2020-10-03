package com.hotmartchalenge.marketplace.domain.exceptions;

public class CategoryNotFoundException extends EntityNotFoundException {
  private static final long serialVersionUID = 1L;

  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException(Long id) {
    this(String.format("There is no category with id %d", id));
  }
}
