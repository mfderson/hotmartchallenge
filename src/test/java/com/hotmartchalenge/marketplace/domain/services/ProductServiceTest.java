package com.hotmartchalenge.marketplace.domain.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.exceptions.BusinessException;
import com.hotmartchalenge.marketplace.domain.exceptions.CategoryNotFoundException;
import com.hotmartchalenge.marketplace.domain.exceptions.ProductNotFoundException;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductServiceTest {

  @Autowired private ProductService productService;

  @MockBean private ProductRepository mockedProductRepository;

  @MockBean private CategoryRepository mockedCategoryRepository;

  private static final Long CATEGORY_EXISTING_ID = 1L;
  private static final Long PRODUCT_EXISTING_ID = 1L;
  private static final Long PRODUCT_NONEXISTING_ID = 1000L;

  private static Product mockedProduct;
  private static Category mockedCategory;

  @BeforeAll
  static void setup() {
    mockedCategory = new Category();
    mockedCategory.setId(CATEGORY_EXISTING_ID);
    mockedCategory.setName("Smartphone");

    mockedProduct = new Product();
    mockedProduct.setId(PRODUCT_EXISTING_ID);
    mockedProduct.setCategory(mockedCategory);
    mockedProduct.setName("Galaxy M31");
    mockedProduct.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut scelerisque arcu");
    mockedProduct.setCreatedAt(OffsetDateTime.now());
  }

  @Test
  @DisplayName("Should be able find a product by existing id")
  void shouldBeAbleFindProductByExistingId() {
    doReturn(Optional.of(mockedProduct))
        .when(mockedProductRepository)
        .findById(PRODUCT_EXISTING_ID);

    Product returnedProduct = productService.findById(PRODUCT_EXISTING_ID);
    Assertions.assertNotNull(returnedProduct);
    Assertions.assertEquals(returnedProduct.getId(), PRODUCT_EXISTING_ID);
  }

  @Test
  @DisplayName("Should be able throws exception when not found a product by id")
  void shouldBeAbleThrowsExceptionByNonExistingProductId() {
    doReturn(Optional.empty()).when(mockedProductRepository).findById(PRODUCT_NONEXISTING_ID);

    ProductNotFoundException ex =
        Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> {
              productService.findById(PRODUCT_NONEXISTING_ID);
            });

    Assertions.assertEquals(
        ex.getMessage(), "There is no product with id " + PRODUCT_NONEXISTING_ID);
  }

  @Test
  @DisplayName("Should be able save a correct product")
  void shouldBeAbleSaveCorrectProdoct() {
    doReturn(mockedProduct).when(mockedProductRepository).save(any());

    Product returnedProduct = productService.save(mockedProduct);

    Assertions.assertNotNull(returnedProduct);
    Assertions.assertEquals(PRODUCT_EXISTING_ID, returnedProduct.getId());
  }

  @Test
  @DisplayName("Should not be able save a product with existing name")
  void shouldNotBeAbleSaveProductWithExistingName() {
    Product mockedProductSameName = new Product();
    mockedProductSameName.setName("Galaxy M31");
    mockedProductSameName.setCategory(mockedCategory);
    mockedProductSameName.setDescription(
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut scelerisque arcu");
    mockedProductSameName.setCreatedAt(OffsetDateTime.now());

    doReturn(List.of(mockedProduct))
        .when(mockedProductRepository)
        .findByNameContainingIgnoreCase(mockedProductSameName.getName());

    BusinessException ex =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              productService.save(mockedProductSameName);
            });

    Assertions.assertEquals(ex.getMessage(), "There is already a product with the same name");
  }

  @Test
  @DisplayName("Should not be able save a product with non-existing category")
  void shouldNotBeAbleSaveProductWithNonExistingCategory() {
    doReturn(Optional.empty()).when(mockedCategoryRepository).findById(mockedCategory.getId());

    CategoryNotFoundException ex =
        Assertions.assertThrows(
            CategoryNotFoundException.class,
            () -> {
              productService.save(mockedProduct);
            });

    Assertions.assertEquals(
        ex.getMessage(), "There is no category with id " + mockedCategory.getId());
  }

  @Test
  @DisplayName("Should be able delete a existing product")
  void shouldBeAbleDeleteExistingProduct() {
    doReturn(Optional.of(mockedProduct))
        .when(mockedProductRepository)
        .findById(mockedProduct.getId());

    Assertions.assertDoesNotThrow(() -> productService.delete(mockedProduct.getId()));
    verify(mockedProductRepository, times(1)).delete(mockedProduct);
  }

  @Test
  @DisplayName("Should not be able delete a non-existing product")
  void shouldNotBeAbleDeleteNonExistingProduct() {
    doReturn(Optional.empty()).when(mockedProductRepository).findById(mockedProduct.getId());

    ProductNotFoundException ex =
        Assertions.assertThrows(
            ProductNotFoundException.class,
            () -> {
              productService.delete(mockedProduct.getId());
            });

    Assertions.assertEquals(
        ex.getMessage(), "There is no product with id " + mockedProduct.getId());
    verify(mockedProductRepository, times(0)).delete(mockedProduct);
  }
}
