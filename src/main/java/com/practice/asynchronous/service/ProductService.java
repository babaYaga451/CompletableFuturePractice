package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.stopWatch;

import com.practice.asynchronous.Product;
import com.practice.asynchronous.domain.ProductInfo;
import com.practice.asynchronous.domain.ProductOption;
import com.practice.asynchronous.domain.Review;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ProductService {

  private ProductInfoService productInfoService = new ProductInfoService();
  private ReviewService reviewService = new ReviewService();
  private InventoryService inventoryService = new InventoryService();

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
        CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

    return cf_productInfo.thenCombine(cf_review,
            ((productInfo, review) -> new Product(productId, productInfo, review)))
        .join();
  }

  private List<ProductOption> updateInventory(ProductInfo productInfo) {
    List<CompletableFuture<ProductOption>> productOptionCfList =
        productInfo.getProductOptions()
            .stream()
            .map(
                productOption ->
                    CompletableFuture.supplyAsync(
                            () -> inventoryService.retrieveInventory(productOption))
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
