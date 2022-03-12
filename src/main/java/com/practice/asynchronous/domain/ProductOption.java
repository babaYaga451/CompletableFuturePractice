package com.practice.asynchronous.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductOption {

  private Integer productionOptionId;
  private String size;
  private String  color;
  private double  price;
  private Inventory inventory;

  public ProductOption(Integer productionOptionId, String size, String color, double price) {
    this.productionOptionId = productionOptionId;
    this.size = size;
    this.color = color;
    this.price = price;
  }

}
