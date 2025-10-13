package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.ClassificationView;

import java.util.UUID;

public interface FindingClassificationPort {
    boolean existsById(UUID id);
    ClassificationView findById(UUID id);
}
