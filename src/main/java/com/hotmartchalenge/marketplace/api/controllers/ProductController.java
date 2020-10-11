package com.hotmartchalenge.marketplace.api.controllers;

import com.hotmartchalenge.marketplace.api.assemblers.PageResDtoAssembler;
import com.hotmartchalenge.marketplace.api.assemblers.ProductReqDtoDisassembler;
import com.hotmartchalenge.marketplace.api.assemblers.ProductResDtoAssembler;
import com.hotmartchalenge.marketplace.api.controllers.openapi.ProductControllerOpenapi;
import com.hotmartchalenge.marketplace.api.dtos.request.ProductReqDto;
import com.hotmartchalenge.marketplace.api.dtos.response.PageResDto;
import com.hotmartchalenge.marketplace.api.dtos.response.ProductResDto;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.services.ProductService;
import com.hotmartchalenge.marketplace.infra.dtos.ProductScore;
import com.hotmartchalenge.marketplace.infra.repositories.ProductScoreRepository;
import java.time.LocalDate;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController implements ProductControllerOpenapi {
  @Autowired private ProductService productService;

  @Autowired private ProductScoreRepository productScoreRepository;

  @Autowired private ProductResDtoAssembler productResDtoAssembler;

  @Autowired private ProductReqDtoDisassembler productReqDtoDisassembler;

  @Autowired private PageResDtoAssembler pageResDtoAssembler;

  @GetMapping("/{id}")
  public ProductResDto findById(@PathVariable Long id) {
    Product product = productService.findById(id);

    return productResDtoAssembler.toDto(product);
  }

  @GetMapping
  public PageResDto<ProductScore> listProductOrderedByScoreNameCategoryName(
      @RequestParam(required = false, defaultValue = "") String searchTerm,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate currentDate,
      Pageable pageable) {
    pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

    currentDate = currentDate == null ? LocalDate.now() : currentDate;

    Page<ProductScore> products =
        productScoreRepository.findAllByNameAndDateOrdered(
            searchTerm.toLowerCase(), currentDate, pageable);

    Map<String, Object> filters = Map.of("searchTerm", searchTerm, "currentDate", currentDate);

    PageResDto<ProductScore> pageResDto = pageResDtoAssembler.toDto(products, filters);

    return pageResDto;
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
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    productService.delete(id);
  }
}
