package com.hotmartchalenge.marketplace.api.assemblers;

import com.hotmartchalenge.marketplace.api.dtos.response.PageResDto;
import java.util.Map;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageResDtoAssembler {
  @Autowired private ModelMapper modelMapper;

  public <T> PageResDto<T> toDto(Page<T> page, Map<String, Object> filters) {
    PageResDto<T> pageResDto = modelMapper.map(page, PageResDto.class);
    pageResDto.setFilters(filters);
    return pageResDto;
  }
}
