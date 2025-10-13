package com.sap.movies_service.classifications.application.input;

import com.sap.movies_service.classifications.domain.Classification;

import java.util.List;
import java.util.UUID;

public interface FindClassificationCasePort {
    boolean existsById(UUID id);
    Classification findById(UUID id);
    List<Classification> findAll();
    List<Classification> findAllById(List<UUID> ids);
}
