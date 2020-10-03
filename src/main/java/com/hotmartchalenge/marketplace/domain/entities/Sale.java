package com.hotmartchalenge.marketplace.domain.entities;

import java.time.OffsetDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Sale {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Double rating;

  @CreationTimestamp private OffsetDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;
}
