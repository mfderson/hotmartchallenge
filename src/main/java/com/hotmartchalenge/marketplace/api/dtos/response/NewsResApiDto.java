package com.hotmartchalenge.marketplace.api.dtos.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsResApiDto {
  private List<ArticlesResApiDto> articles;
}
