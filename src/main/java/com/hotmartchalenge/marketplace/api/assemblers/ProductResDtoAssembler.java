package com.hotmartchalenge.marketplace.api.assemblers;

import com.hotmartchalenge.marketplace.api.dtos.response.ProductResDto;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductResDtoAssembler {
  @Autowired private ModelMapper modelMapper;

  public ProductResDto toDto(Product product) {
    return modelMapper.map(product, ProductResDto.class);
  }

  public List<ProductResDto> toCollectionDto(List<Product> products) {
    return products.stream().map(product -> toDto(product)).collect(Collectors.toList());
  }
}
