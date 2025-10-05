package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.domain.Movie;

import java.util.List;

public interface FindGenerePort {
    Movie findById(String id);

    List<Movie> findLikeTitle(String title);

    List<Movie> findAll();
}
