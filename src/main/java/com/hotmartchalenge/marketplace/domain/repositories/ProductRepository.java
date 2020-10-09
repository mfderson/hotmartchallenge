package com.hotmartchalenge.marketplace.domain.repositories;

import com.hotmartchalenge.marketplace.domain.entities.Product;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CustomJpaRepository<Product, Long> {

  @Query(
      value =
          "select p from Product p left join p.category c left join c.news n where (lower(p.name) like '%' || :name || '%') and (n.publishedAt between :startDate and :endDate)")
  Page<Product> findAllByNameAndDate(
      String name, OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable);
}
