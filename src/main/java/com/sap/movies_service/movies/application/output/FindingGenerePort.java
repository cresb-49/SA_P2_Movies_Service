package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Genre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingGenerePort {
    Optional<Genre> findById(UUID id);

    Optional<Genre> findByName(String name);

    List<Genre> findLikeName(String name);

    List<Genre> findAll();
}
