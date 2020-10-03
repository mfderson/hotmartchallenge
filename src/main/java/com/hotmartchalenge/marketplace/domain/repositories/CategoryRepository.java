package com.hotmartchalenge.marketplace.domain.repositories;

import com.hotmartchalenge.marketplace.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
