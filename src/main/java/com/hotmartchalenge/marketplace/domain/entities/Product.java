package com.hotmartchalenge.marketplace.domain.entities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Product {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String description;

  @CreationTimestamp private OffsetDateTime createdAt;

  @ManyToOne(cascade = {CascadeType.DETACH})
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.REMOVE,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<Sale> sales = new ArrayList<>();
}
