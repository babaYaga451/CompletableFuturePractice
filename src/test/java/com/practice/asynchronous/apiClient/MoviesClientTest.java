package com.practice.asynchronous.apiClient;

import com.practice.asynchronous.domain.movies.Movie;
import com.practice.asynchronous.util.CommonUtil;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

public class MoviesClientTest {

  WebClient webClient = WebClient.builder()
      .baseUrl("http://localhost:8080/movies")
      .build();

  MoviesClient moviesClient = new MoviesClient(webClient);

  @BeforeEach
  public void setUp(){
    CommonUtil.stopWatch.reset();
  }

  @Test
  public void retrieveMovie(){
    CommonUtil.startTimer();
    long movieInfoId = 1L;
    Movie movie = moviesClient.retrieveMovie(movieInfoId);

    CommonUtil.timeTaken();
    System.out.println("Movie : "+ movie);
    Assertions.assertNotNull(movie);
    Assertions.assertEquals("Batman Begins", movie.getMovieInfo().getName());
    Assertions.assertEquals(1, movie.getReviewList().size());
  }

  @Test
  public void retrieveMovie_CF(){
    CommonUtil.startTimer();
    long movieInfoId = 1L;
    Movie movie = moviesClient.retrieveMovie_CF(movieInfoId)
        .join();

    CommonUtil.timeTaken();
    System.out.println("Movie : "+ movie);
    Assertions.assertNotNull(movie);
    Assertions.assertEquals("Batman Begins", movie.getMovieInfo().getName());
    Assertions.assertEquals(1, movie.getReviewList().size());
  }

  @RepeatedTest(5)
  public void retrieveMovieList(){
    CommonUtil.startTimer();
    List<Long> movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    List<Movie> movieList = moviesClient.retrieveMovieList(movieInfoIds);

    CommonUtil.timeTaken();
    System.out.println("Movie List: "+ movieList);
    Assertions.assertNotNull(movieList);
    Assertions.assertEquals(7, movieList.size());
  }

  @RepeatedTest(5)
  public void retrieveMovieList_CF(){
    CommonUtil.startTimer();
    List<Long> movieInfoIds = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L);
    List<Movie> movieList = moviesClient.retrieveMovieList_CF(movieInfoIds);

    CommonUtil.timeTaken();
    System.out.println("Movie List: "+ movieList);
    Assertions.assertNotNull(movieList);
    Assertions.assertEquals(7, movieList.size());
  }
}
