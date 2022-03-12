package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.stopWatch;
import static com.practice.asynchronous.util.LoggerUtil.log;

import com.practice.asynchronous.domain.Inventory;
import com.practice.asynchronous.domain.Product;
import com.practice.asynchronous.domain.ProductInfo;
import com.practice.asynchronous.domain.ProductOption;
import com.practice.asynchronous.domain.Review;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProductService {

  private ProductInfoService productInfoService;
  private ReviewService reviewService;
  private InventoryService inventoryService;

  public ProductService(ProductInfoService productInfoService, ReviewService reviewService,
      InventoryService inventoryService) {
    this.inventoryService = inventoryService;
    this.productInfoService = productInfoService;
    this.reviewService = reviewService;
  }

  public Product retrieveProductDetailsWithInventory(String productId) {
    stopWatch.start();
    CompletableFuture<ProductInfo> cf_productInfo =
        CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
            .thenApply(productInfo -> {
              productInfo.setProductOptions(updateInventory(productInfo));
              return productInfo;
            });

    CompletableFuture<Review> cf_review =
        CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId))
            .exceptionally(e -> {
              log("Handled exception in review service : " + e.getMessage());
              return Review.builder()
                  .noOfReviews(0).overallRating(0.0)
                  .build();
            });
    stopWatch.stop();
    log("Total time taken : "+ stopWatch.getTime());
    return cf_productInfo
        .thenCombine(cf_review,
            ((productInfo, review) -> new Product(productId, productInfo, review)))
        .whenComplete(
            (product, ex) -> log("Inside when complete " + product + " exception is : " + ex))
        .join();
  }

  private List<ProductOption> updateInventory(ProductInfo productInfo) {
    List<CompletableFuture<ProductOption>> productOptionCfList =
        productInfo.getProductOptions()
            .stream()
            .map(productOption ->
                CompletableFuture.supplyAsync(
                        () -> inventoryService.retrieveInventory(productOption))
                    .exceptionally(ex -> {
                      log("Handled exception in Inventory service : "+ ex.getMessage());
                      return Inventory.builder()
                          .count(1)
                          .build();
                    })
                    .thenApply(inventory -> {
                          productOption.setInventory(inventory);
                          return productOption;
                        }
                    )
            )
            .collect(Collectors.toList());

    return productOptionCfList
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }
}
