package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Movie;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingMoviePort {

    boolean existsById(UUID id);

    Optional<Movie> findById(UUID id);

    Page<Movie> findAll(int page);

    Page<Movie> findLikeTitle(String title, int page);

    List<Movie> findByIdsIn(List<UUID> ids);

    boolean hasMoviesWithClassificationId(UUID id);
}
