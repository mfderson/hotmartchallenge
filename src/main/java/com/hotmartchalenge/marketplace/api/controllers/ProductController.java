package com.hotmartchalenge.marketplace.api.controllers;

import javax.websocket.server.PathParam;

import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
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
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  @GetMapping("/{id}")
  public Product findById(@PathVariable Long id) throws Exception {
    return productRepository.findById(id).orElseThrow(
      () -> new Exception(String.format("Product with id %d non-exists", id)));
  }
  
}
