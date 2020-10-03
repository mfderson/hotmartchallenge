package com.hotmartchalenge.marketplace.api.assemblers;

import com.hotmartchalenge.marketplace.api.dtos.request.ProductReqDto;
import com.hotmartchalenge.marketplace.domain.entities.Product;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductReqDtoDisassembler {
  @Autowired
  private ModelMapper modelMapper;

  public Product toEntityObject(ProductReqDto productReqDto) {
    return modelMapper.map(productReqDto, Product.class);
  }

  public void copyToEntityObject(ProductReqDto productReqDto, Product product) {
    modelMapper.map(productReqDto, product);
  }
}
