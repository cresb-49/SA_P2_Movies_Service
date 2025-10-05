package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingMoviePort {
    Optional<Movie> findById(UUID id);

    List<Movie> findAll();

    List<Movie> findLikeTitle(String title);
}
