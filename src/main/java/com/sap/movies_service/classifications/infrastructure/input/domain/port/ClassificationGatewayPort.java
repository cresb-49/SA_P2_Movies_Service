package com.sap.movies_service.classifications.infrastructure.input.domain.port;

import com.sap.movies_service.classifications.domain.Classification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassificationGatewayPort {
    boolean existsById(UUID id);
    Optional<Classification> findById(UUID id);
    List<Classification> findByIds(List<UUID> ids);
}
