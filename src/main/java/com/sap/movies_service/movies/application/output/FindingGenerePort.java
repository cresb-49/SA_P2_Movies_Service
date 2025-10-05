package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Genere;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindingGenerePort {
    Optional<Genere> findById(UUID id);

    List<Genere> findLikeName(String name);

    List<Genere> findAll();
}
