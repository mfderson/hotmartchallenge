package com.hotmartchalenge.marketplace.api.dtos.response;

import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResDto {
  private Long id;
  private String name;
  private String description;
  private OffsetDateTime createdAt;
}
