package com.hotmartchalenge.marketplace.domain.entities;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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
import javax.persistence.PostLoad;
import javax.persistence.Transient;
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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
  private List<Sale> sales = new ArrayList<>();

  @Transient private Double score;

  @PostLoad
  private void setScoreWithCalculatedValues() {
    score = calculateAverageLastTwelveMonths() + calculateSalesPerDay();
  }

  public Double calculateAverageLastTwelveMonths() {
    double sumRating = 0.0;
    OffsetDateTime datetimeTwelveMonthsAgo = OffsetDateTime.now().minusMonths(12L);
    int numberOfSales = 0;

    for (Sale sale : this.getSales()) {
      if (datetimeTwelveMonthsAgo.compareTo(sale.getCreatedAt()) >= 0) {
        numberOfSales++;
        sumRating += sale.getRating();
      }
    }

    return numberOfSales > 0 ? sumRating / numberOfSales : 0.0;
  }

  public Double calculateSalesPerDay() {
    Long numberOfDays = ChronoUnit.DAYS.between(OffsetDateTime.now(), getCreatedAt());

    return numberOfDays > 0 ? getSales().size() / numberOfDays : 0.0;
  }
}
