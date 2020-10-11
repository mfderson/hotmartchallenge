package com.hotmartchalenge.marketplace.api.controllers.openapi;

import com.hotmartchalenge.marketplace.api.dtos.request.ProductReqDto;
import com.hotmartchalenge.marketplace.api.dtos.response.PageResDto;
import com.hotmartchalenge.marketplace.api.dtos.response.ProductResDto;
import com.hotmartchalenge.marketplace.api.exceptionHandlers.ErrorMessage;
import com.hotmartchalenge.marketplace.infra.dtos.ProductScore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

@Api(tags = "Products")
public interface ProductControllerOpenapi {
  @ApiOperation("Find product by id")
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid product id", response = ErrorMessage.class),
    @ApiResponse(code = 404, message = "Product not found", response = ErrorMessage.class)
  })
  public ProductResDto findById(@ApiParam(value = "Product id", example = "1") Long id);

  @ApiOperation("Find products by name")
  public PageResDto<ProductScore> listProductOrderedByScoreNameCategoryName(
      @ApiParam(value = "Part of product name", example = "Smartphone") String searchTerm,
      @ApiParam(value = "Current date", example = "2020-10-07") LocalDate currentDate,
      Pageable pageable);

  @ApiOperation("Save a product")
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid data", response = ErrorMessage.class),
    @ApiResponse(code = 404, message = "Category not found", response = ErrorMessage.class)
  })
  public ProductResDto insert(
      @ApiParam(name = "Body", value = "Json to save a new product") ProductReqDto productReqDto);

  @ApiOperation("Update a product by id")
  @ApiResponses({
    @ApiResponse(code = 400, message = "Invalid data", response = ErrorMessage.class),
    @ApiResponse(code = 200, message = "Product updated", response = ErrorMessage.class),
    @ApiResponse(code = 404, message = "Product not found", response = ErrorMessage.class)
  })
  public ProductResDto update(
      @ApiParam(value = "Product id", example = "1") Long id,
      @ApiParam(name = "Body", value = "Json to update a existing product")
          ProductReqDto productReqDto);

  @ApiOperation("Delete a product by id")
  @ApiResponses({
    @ApiResponse(code = 204, message = "Deleted product", response = ErrorMessage.class),
    @ApiResponse(code = 404, message = "Product not found", response = ErrorMessage.class)
  })
  public void delete(@ApiParam(value = "Product id", example = "1") Long id);
}
