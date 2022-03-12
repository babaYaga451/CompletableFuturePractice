package com.practice.asynchronous.service;

import static com.practice.asynchronous.util.CommonUtil.delay;

import com.practice.asynchronous.domain.Review;


public class ReviewService {

  public Review retrieveReviews(String productId) {
    delay(1000);
    return new Review(200, 4.5);
  }
}