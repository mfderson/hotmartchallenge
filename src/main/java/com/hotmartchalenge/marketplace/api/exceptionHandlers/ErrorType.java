package com.hotmartchalenge.marketplace.api.exceptionHandlers;

import lombok.Getter;

@Getter
public enum ErrorType {
  RESOURCE_NOT_FOUND("Resource not found"),
  ENTITY_IN_USE("Entity in use"),
  BUSINESS_ERROR("Business error"),
  JSON_SYNTAX_ERROR("JSON sintax error"),
  INVALID_PARAMETER("Invalid parameter"),
  SYSTEM_ERROR("System error"),
  INVALID_DATA("Invalid data"),
  INVALID_PARAMETER_FORMAT("Invalid parameter format");

  private final String title;

  ErrorType(String title) {
    this.title = title;
  }
}
