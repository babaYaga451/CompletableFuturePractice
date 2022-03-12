package com.practice.asynchronous;

import com.practice.asynchronous.domain.ProductInfo;
import com.practice.asynchronous.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

  private String productId;
  private ProductInfo productInfo;
  private Review review;
}
