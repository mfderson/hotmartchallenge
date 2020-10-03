package com.hotmartchalenge.marketplace.api.dtos.request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReqDto {

  @NotBlank private String name;

  @NotBlank private String description;

  @Valid @NotNull private CategoryIdReqDto category;
}
