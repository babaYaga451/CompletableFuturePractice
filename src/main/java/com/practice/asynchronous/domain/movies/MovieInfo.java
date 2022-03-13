package com.practice.asynchronous.domain.movies;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {
  private String movieInfoId;
  private String name;
  private Integer year;

  private List<String> cast;
  private LocalDate release_date;
}