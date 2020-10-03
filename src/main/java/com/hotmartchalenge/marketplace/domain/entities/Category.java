package com.hotmartchalenge.marketplace.domain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Category {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
}
