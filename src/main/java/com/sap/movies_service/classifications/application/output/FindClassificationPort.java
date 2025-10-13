package com.sap.movies_service.classifications.application.output;

import com.sap.movies_service.classifications.domain.Classification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindClassificationPort {
    boolean existsById(UUID id);
    Optional<Classification> findById(UUID id);
    Optional<Classification> findByNameIgnoreCase(String name);
    List<Classification> findAll();
    List<Classification> findAllById(List<UUID> ids);
}
