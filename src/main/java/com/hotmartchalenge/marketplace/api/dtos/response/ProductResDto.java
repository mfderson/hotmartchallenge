package com.hotmartchalenge.marketplace.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResDto {
  private Long id;
  private String name;
  private String description;
  @JsonIgnore private String categoryName;
  private OffsetDateTime createdAt;
  private Double score;
}
