package com.hotmartchalenge.marketplace.domain.repositories;

import com.hotmartchalenge.marketplace.domain.entities.News;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

  @Query(
      value =
          "select n from News n inner join n.category c where c.id = :categoryId AND n.publishedAt between :startDate AND :endDate")
  public List<News> getAllBetweenDates(
      @Param("categoryId") Long categoryId,
      @Param("startDate") OffsetDateTime startDate,
      @Param("endDate") OffsetDateTime endDate);

  // @Query(value = "from News n where n.category.id = :categoryId")
  // public List<News> getAllBetweenDates(@Param("categoryId") Long categoryId);
}
