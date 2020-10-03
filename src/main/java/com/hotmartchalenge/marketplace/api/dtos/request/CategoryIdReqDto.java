package com.hotmartchalenge.marketplace.api.dtos.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryIdReqDto {
  @NotNull private Long id;
}
