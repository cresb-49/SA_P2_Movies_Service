package com.sap.movies_service.movies.infrastructure.input.domain.port;

import com.sap.movies_service.movies.domain.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieGatewayPort {
    boolean hasMoviesWithClassificationId(UUID id);

    Optional<Movie> findById(UUID id);

    boolean existsById(UUID id);

    List<Movie> findByIds(List<UUID> ids);
}
