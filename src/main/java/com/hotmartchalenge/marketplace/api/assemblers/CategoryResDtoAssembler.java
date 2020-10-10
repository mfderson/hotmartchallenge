package com.hotmartchalenge.marketplace.api.assemblers;

import com.hotmartchalenge.marketplace.api.dtos.response.CategoryResDto;
import com.hotmartchalenge.marketplace.domain.entities.Category;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryResDtoAssembler {
  @Autowired private ModelMapper modelMapper;

  public CategoryResDto toDto(Category category) {
    return modelMapper.map(category, CategoryResDto.class);
  }

  public List<CategoryResDto> toCollectionDto(List<Category> categories) {
    return categories.stream().map(category -> toDto(category)).collect(Collectors.toList());
  }
}
