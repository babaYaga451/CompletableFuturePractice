package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.delay;

import com.practice.asynchronous.domain.ProductInfo;
import com.practice.asynchronous.domain.ProductOption;
import java.util.List;

public class ProductInfoService {

  public ProductInfo retrieveProductInfo(String productId) {
    delay(1000);
    List<ProductOption> productOptions = List.of(new ProductOption(1, "64GB", "Black", 699.99),
        new ProductOption(2, "128GB", "Black", 749.99));
    return ProductInfo.builder().productId(productId)
        .productOptions(productOptions)
        .build();
  }
}