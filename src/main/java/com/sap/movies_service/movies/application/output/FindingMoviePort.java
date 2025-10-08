package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Movie;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingMoviePort {
    Optional<Movie> findById(UUID id);

    Page<Movie> findAll(int page);

    Page<Movie> findLikeTitle(String title, int page);

    Page<Movie> findByGenereId(UUID genereId, int page);

    List<Movie> findByIdsIn(List<UUID> ids);
}
