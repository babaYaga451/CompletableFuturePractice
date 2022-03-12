package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.delay;

import com.practice.asynchronous.domain.Inventory;
import com.practice.asynchronous.domain.ProductOption;
import java.util.concurrent.CompletableFuture;

public class InventoryService {

  public Inventory retrieveInventory(ProductOption productOption) {
    delay(500);
    return Inventory.builder()
        .count(2).build();

  }
}