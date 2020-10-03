package com.hotmartchalenge.marketplace.api.controllers;

import com.hotmartchalenge.marketplace.api.assemblers.ProductReqDtoDisassembler;
import com.hotmartchalenge.marketplace.api.assemblers.ProductResDtoAssembler;
import com.hotmartchalenge.marketplace.api.dtos.response.ProductResDto;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  @Autowired
  private ProductService productService;

  @Autowired
  private ProductResDtoAssembler productResDtoAssembler;

  @GetMapping("/{id}")
  public ProductResDto findById(@PathVariable Long id) {
    Product product = productService.findById(id);

    return productResDtoAssembler.toDto(product);
  }
}
