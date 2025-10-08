package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.domain.Movie;

import java.util.List;
import java.util.UUID;

public interface FindGenerePort {
    Genre findById(UUID id);

    List<Genre> findLikeTitle(String title);

    List<Genre> findAll();
}
