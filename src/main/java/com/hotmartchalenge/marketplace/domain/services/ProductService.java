package com.hotmartchalenge.marketplace.domain.services;

import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.exceptions.BusinessException;
import com.hotmartchalenge.marketplace.domain.exceptions.CategoryNotFoundException;
import com.hotmartchalenge.marketplace.domain.exceptions.ProductNotFoundException;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
  @Autowired private ProductRepository productRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Value("${marketplace.domain.product-name-size}")
  private int nameMaxSize;

  @Value("${marketplace.domain.product-description-size}")
  private int descriptionMaxSize;

  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Transactional
  public Product save(Product product) {
    productRepository.detach(product);

    if (nameSizeGreaterThanMax(product)) {
      throw new BusinessException("Name of product must be less than " + nameMaxSize);
    }

    if (descriptionSizeGreaterThanMax(product)) {
      throw new BusinessException("Description of product must be less than " + descriptionMaxSize);
    }

    Category category = findCategoryById(product.getCategory().getId());
    product.setCategory(category);

    if (existProductWithSameName(product.getName())) {
      throw new BusinessException("There is already a product with the same name");
    }

    return productRepository.save(product);
  }

  @Transactional
  public void delete(Long id) {
    Product product = findById(id);
    productRepository.delete(product);
  }

  private Category findCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }

  private boolean existProductWithSameName(String name) {
    return !productRepository.findByNameContainingIgnoreCase(name).isEmpty();
  }

  private boolean nameSizeGreaterThanMax(Product product) {
    return product.getName().length() > nameMaxSize;
  }

  private boolean descriptionSizeGreaterThanMax(Product product) {
    return product.getDescription().length() > nameMaxSize;
  }
}
