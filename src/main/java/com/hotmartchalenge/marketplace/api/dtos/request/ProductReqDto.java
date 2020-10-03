package com.hotmartchalenge.marketplace.api.dtos.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductReqDto {

  @NotBlank private String name;

  @NotBlank private String description;
}
