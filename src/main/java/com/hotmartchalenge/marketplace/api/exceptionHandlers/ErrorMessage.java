package com.hotmartchalenge.marketplace.api.exceptionHandlers;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ErrorMessage {
  private final LocalDateTime timestamp;
  private final Integer status;
  private final String type;
  private final String title;
  private final String detail;
  private final String userMessage;
  private final List<Object> objects;

  @Getter
  @Builder
  public static class Object {
    private final String name;
    private final String userMessage;
  }
}
