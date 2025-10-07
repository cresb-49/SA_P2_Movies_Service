package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.domain.Movie;

import java.util.List;
import java.util.UUID;

public interface FindMoviePort {
    Movie findById(String id);

    List<Movie> findByTitle(String title);

    List<Movie> findByGenere(UUID genereId);

    List<Movie> findAll();
}
