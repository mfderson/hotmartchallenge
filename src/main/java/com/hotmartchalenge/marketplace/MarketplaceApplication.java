package com.hotmartchalenge.marketplace;

import com.hotmartchalenge.marketplace.infra.repositories.CustomJpaRepositoryImpl;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class MarketplaceApplication {

  public static void main(String[] args) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

    SpringApplication.run(MarketplaceApplication.class, args);
  }
}
