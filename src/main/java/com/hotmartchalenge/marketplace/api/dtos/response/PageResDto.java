package com.hotmartchalenge.marketplace.api.dtos.response;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResDto<T> {
  private Map<String, Object> filters;
  private List<T> content;
  private Integer size;
  private Long totalElements;
  private Integer totalPages;
  private Integer number;
}
