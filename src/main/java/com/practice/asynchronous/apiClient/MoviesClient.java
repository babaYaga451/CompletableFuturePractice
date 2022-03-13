package com.practice.asynchronous.apiClient;

import com.practice.asynchronous.domain.movies.Movie;
import com.practice.asynchronous.domain.movies.MovieInfo;
import com.practice.asynchronous.domain.movies.Review;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

public class MoviesClient {

  private final WebClient webClient;

  public MoviesClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public Movie retrieveMovie(long movieId){
    MovieInfo movieInfo = invokeMovieInfoService(movieId);
    List<Review> reviewList = invokeReviewService(movieId);

    return new Movie(movieInfo,reviewList);
  }

  public CompletableFuture<Movie> retrieveMovie_CF(long movieId){
    CompletableFuture<MovieInfo> movieInfo = CompletableFuture.supplyAsync(
        () -> invokeMovieInfoService(movieId));
    CompletableFuture<List<Review>> reviewList = CompletableFuture.supplyAsync(
        () -> invokeReviewService(movieId));

    return movieInfo.thenCombine(reviewList, Movie::new);
  }

  public List<Movie> retrieveMovieList(List<Long> movieIds){
    return movieIds
        .stream()
        .map(this::retrieveMovie)
        .collect(Collectors.toList());
  }

  public List<Movie> retrieveMovieList_CF(List<Long> movieIds){
    List<CompletableFuture<Movie>> movieList = movieIds
        .stream()
        .map(this::retrieveMovie_CF)
        .collect(Collectors.toList());

    return movieList
        .stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
  }

  private List<Review> invokeReviewService(long movieInfoId) {

    String reviewInfoUrlPath = UriComponentsBuilder
        .fromPath("/v1/reviews")
        .queryParam("movieInfoId", movieInfoId)
        .buildAndExpand()
        .toString();


    return webClient.get()
        .uri(reviewInfoUrlPath)
        .retrieve()
        .bodyToFlux(Review.class)
        .collectList()
        .block();
  }

  private MovieInfo invokeMovieInfoService(long movieInfoId) {

    String moviesInfoUrlPath = "/v1/movie_infos/{movieInfoId}";

    return webClient
        .get()
        .uri(moviesInfoUrlPath, movieInfoId)
        .retrieve()
        .bodyToMono(MovieInfo.class)
        .block();
  }
}
