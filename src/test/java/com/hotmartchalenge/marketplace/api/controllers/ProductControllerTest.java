package com.hotmartchalenge.marketplace.api.controllers;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;

import com.hotmartchalenge.marketplace.api.exceptionHandlers.ApiExceptionHandler;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.exceptions.ProductNotFoundException;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
import com.hotmartchalenge.marketplace.domain.services.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;
import io.restassured.http.ContentType;

@WebMvcTest
public class ProductControllerTest {
  private final Long EXISTING_ID = 1L;
  private final Long NON_EXISTING_ID = 10111L;
  private final Long NEGATIVE_ID = -1L;

  @Autowired
  private ProductController productController;

  @Autowired
  private ApiExceptionHandler apiExceptionHandler;

  @MockBean
  private ProductService mockedProductService;

  @MockBean
  private ProductRepository mockedProductRepository;

  @BeforeEach
  void setup() {
    standaloneSetup(productController, apiExceptionHandler);
  }

  @Test
  @DisplayName("Should be able return ok when find a existing product")
  void shouldBeAbleReturnOk_whenFindExistingProduct() {
    Product product = new Product();
    product.setId(EXISTING_ID);
    product.setName("Galaxy S20");
    product.setDescription("Celular da Samsung top de linha");
    product.setCreatedAt(OffsetDateTime.now());

    when(mockedProductRepository.findById(EXISTING_ID))
      .thenReturn(Optional.of(product));

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/products/{id}", EXISTING_ID)
    .then()
      .statusCode(HttpStatus.OK.value());
  }

  @Test
  @DisplayName("Should be able return not found when find a non-existing product")
  void shouldBeAbleReturnNotFound_whenFindNonExistingProduct() {
    when(mockedProductRepository.findById(anyLong()))
      .thenThrow(new ProductNotFoundException(NON_EXISTING_ID));

    given()
      .accept(ContentType.JSON)
    .when()
      .get("/products/{id}", NON_EXISTING_ID)
    .then()
      .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  @DisplayName("Should be able return bad request when find a invalid product id")
  void shouldBeAbleReturnBadRequestWhenFindInvalidProductId() {
    given()
      .accept(ContentType.JSON)
    .when()
      .get("/products/{id}", NEGATIVE_ID)
    .then()
      .statusCode(HttpStatus.BAD_REQUEST.value());

    verify(mockedProductRepository, never()).findById(NEGATIVE_ID);
  }
}
