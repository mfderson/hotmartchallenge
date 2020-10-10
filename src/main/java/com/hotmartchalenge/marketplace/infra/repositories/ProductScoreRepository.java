package com.hotmartchalenge.marketplace.infra.repositories;

import com.hotmartchalenge.marketplace.domain.entities.Product;
import com.hotmartchalenge.marketplace.infra.dtos.ProductScore;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductScoreRepository extends JpaRepository<Product, Long> {

  @Query(
      value =
          "select p.id as id, "
              + "p.name as name, "
              + "p.description as description, "
              + "p.created_at as createdAt, "
              + "round(cast((coalesce(avg(s.rating), 0) + coalesce(count(s.id)/date_part('day', AGE(now(), p.created_at)), 0) + coalesce(count(n.id))) as numeric),2) as score "
              + "from product p "
              + "left join category c on p.category_id = c.id "
              + "left join news n on c.id = n.category_id "
              + "and date(n.published_at) = :currentDate "
              + "left join sale s on p.id = s.product_id "
              + "and s.created_at > (p.created_at - interval '1 year') "
              + "where lower(p.name) like :name% "
              + "group by p.id, c.name "
              + "order by score desc, p.name, c.name",
      nativeQuery = true)
  Page<ProductScore> findAllByNameAndDateOrdered(
      String name, LocalDate currentDate, Pageable pageable);
}
