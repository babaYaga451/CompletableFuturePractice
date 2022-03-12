package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.stopWatch;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.practice.asynchronous.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductServiceTest {
  @Mock
  InventoryService inventoryService;
  @Mock
  ReviewService reviewService;
  @Mock
  ProductInfoService productInfoService;
  @InjectMocks
  ProductService productService;

  @BeforeEach
  public void setUp(){
    stopWatch.reset();
  }

  @Test
  void retrieveProductDetailsWithInventory() {
    String productId = "ABC12345";
    when(reviewService.retrieveReviews(any())).thenCallRealMethod();
    when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
    when(inventoryService.retrieveInventory(any())).thenCallRealMethod();
    Product product = productService.retrieveProductDetailsWithInventory(productId);

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    product.getProductInfo().getProductOptions()
        .forEach(
            productOption -> assertNotNull(productOption.getInventory())
        );
    assertNotNull(product.getReview());
  }

  @Test
  void retrieveProductDetailsWithInventory_WithException() {
    String productId = "ABC12345";
    when(reviewService.retrieveReviews(any())).thenThrow(
        new RuntimeException("Exception occurred"));
    when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
    when(inventoryService.retrieveInventory(any())).thenCallRealMethod();
    Product product = productService.retrieveProductDetailsWithInventory(productId);

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    product.getProductInfo().getProductOptions()
        .forEach(
            productOption -> assertNotNull(productOption.getInventory())
        );
    assertEquals(0, product.getReview().getNoOfReviews());
  }

  @Test
  void retrieveProductDetailsWithInventory_productInfoServiceError() {
    String productId = "ABC12345";
    when(reviewService.retrieveReviews(any())).thenCallRealMethod();
    when(productInfoService.retrieveProductInfo(any())).thenThrow(
        new RuntimeException("Exception Occurred"));
    assertThrows(RuntimeException.class,
        () -> productService.retrieveProductDetailsWithInventory(productId));
  }

  @Test
  void retrieveProductDetailsWithInventory_WithInventoryServiceError() {
    String productId = "ABC12345";
    when(reviewService.retrieveReviews(any())).thenCallRealMethod();
    when(productInfoService.retrieveProductInfo(any())).thenCallRealMethod();
    when(inventoryService.retrieveInventory(any())).thenThrow(new RuntimeException("Exception occurred"));
    Product product = productService.retrieveProductDetailsWithInventory(productId);

    assertNotNull(product);
    assertTrue(product.getProductInfo().getProductOptions().size() > 0);
    product.getProductInfo().getProductOptions()
        .forEach(
            productOption -> assertEquals(1, productOption.getInventory().getCount())
        );
    assertNotNull(product.getReview());
  }
}