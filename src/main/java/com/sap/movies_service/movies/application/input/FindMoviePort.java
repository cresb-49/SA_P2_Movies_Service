package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.domain.Movie;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FindMoviePort {
    Movie findById(String id);

    Page<Movie> findByTitle(String title, int page);

    Page<Movie> findAll(int page);

    List<Movie> findByIdsIn(List<UUID> ids);
}
