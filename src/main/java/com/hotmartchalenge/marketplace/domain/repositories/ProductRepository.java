package com.hotmartchalenge.marketplace.domain.repositories;

import com.hotmartchalenge.marketplace.domain.entities.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CustomJpaRepository<Product, Long> {}
