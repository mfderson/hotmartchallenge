package com.hotmartchalenge.marketplace.api.controllers;

import com.hotmartchalenge.marketplace.api.assemblers.ProductReqDtoDisassembler;
import com.hotmartchalenge.marketplace.api.assemblers.ProductResDtoAssembler;
import com.hotmartchalenge.marketplace.api.dtos.request.ProductReqDto;
import com.hotmartchalenge.marketplace.api.dtos.response.ProductResDto;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
import com.hotmartchalenge.marketplace.domain.services.ProductService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
  @Autowired private ProductService productService;

  @Autowired private ProductRepository productRepository;

  @Autowired private ProductResDtoAssembler productResDtoAssembler;

  @Autowired private ProductReqDtoDisassembler productReqDtoDisassembler;

  @GetMapping("/{id}")
  public ProductResDto findById(@PathVariable Long id) {
    Product product = productService.findById(id);

    return productResDtoAssembler.toDto(product);
  }

  @GetMapping
  public Page<ProductResDto> list(Pageable pageable) {
    Page<Product> productsPage = productRepository.findAll(pageable);

    List<ProductResDto> productsList =
        productResDtoAssembler.toCollectionDto(productsPage.getContent());

    Collections.sort(
        productsList,
        Comparator.comparing(ProductResDto::getScore).thenComparing(ProductResDto::getName));

    Page<ProductResDto> productResDtoPage =
        new PageImpl<>(productsList, pageable, productsPage.getTotalElements());

    return productResDtoPage;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProductResDto insert(@RequestBody @Valid ProductReqDto productReqDto) {
    Product product = productReqDtoDisassembler.toEntityObject(productReqDto);

    return productResDtoAssembler.toDto(productService.save(product));
  }

  @PutMapping("/{id}")
  public ProductResDto update(
      @PathVariable Long id, @RequestBody @Valid ProductReqDto productReqDto) {
    Product product = productService.findById(id);
    productReqDtoDisassembler.copyToEntityObject(productReqDto, product);

    return productResDtoAssembler.toDto(productService.save(product));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    productService.delete(id);
  }
}
