package com.hotmartchalenge.marketplace.domain.services;

import com.hotmartchalenge.marketplace.domain.entities.Category;
import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.domain.exceptions.CategoryNotFoundException;
import com.hotmartchalenge.marketplace.domain.exceptions.ProductNotFoundException;
import com.hotmartchalenge.marketplace.domain.repositories.CategoryRepository;
import com.hotmartchalenge.marketplace.domain.repositories.ProductRepository;
import com.hotmartchalenge.marketplace.utils.FormatDatetimeUtils;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
  @Autowired private ProductRepository productRepository;

  @Autowired private CategoryRepository categoryRepository;

  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
  }

  public Page<Product> findAllByNameAndDate(String name, String date, Pageable pageable) {

    OffsetDateTime startDate = getTodayIfDateEmpty(date);
    OffsetDateTime endDate = FormatDatetimeUtils.convertTimeToEndDay(startDate);

    return productRepository.findAllByNameAndDate(name.toLowerCase(), startDate, endDate, pageable);
  }

  @Transactional
  public Product save(Product product) {
    productRepository.detach(product);

    Category category = findCategoryById(product.getCategory().getId());
    product.setCategory(category);

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

  private OffsetDateTime getTodayIfDateEmpty(String date) {
    if (date.isEmpty()) {
      return FormatDatetimeUtils.convertTimeToStartDay(OffsetDateTime.now());
    }

    return FormatDatetimeUtils.convertTimeToStartDay(date);
  }
}
