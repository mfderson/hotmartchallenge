package com.hotmartchalenge.marketplace.infra.dtos;

import java.time.LocalDateTime;

public interface ProductScore {
  Long getId();

  String getName();

  String getDescription();

  LocalDateTime getCreatedAt();

  Float getScore();
}
