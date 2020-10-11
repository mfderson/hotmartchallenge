package com.hotmartchalenge.marketplace.api.exceptionHandlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ErrorMessage {

  @ApiModelProperty(example = "2020-10-11T11:35:11.133+00:00")
  private final LocalDateTime timestamp;

  @ApiModelProperty(example = "400")
  private final Integer status;

  private final String type;

  @ApiModelProperty(example = "Invalid data")
  private final String title;

  @ApiModelProperty(example = "One or more fields are invalid, fill correctly and try again.")
  private final String detail;

  @ApiModelProperty(example = "One or more fields are invalid, fill correctly and try again.")
  private final String userMessage;

  @ApiModelProperty("List of Objects or fields with errors")
  private final List<Object> objects;

  @ApiModel("ErrorMessageObject")
  @Getter
  @Builder
  public static class Object {
    private final String name;
    private final String userMessage;
  }
}
