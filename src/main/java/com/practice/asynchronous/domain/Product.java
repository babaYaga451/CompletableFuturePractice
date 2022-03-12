package com.practice.asynchronous.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class Product {

  @NonNull
  private String productId;
  @NonNull
  private ProductInfo productInfo;
  @NonNull
  private Review review;

}
