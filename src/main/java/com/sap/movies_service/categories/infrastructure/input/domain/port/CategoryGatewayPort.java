package com.sap.movies_service.categories.infrastructure.input.domain.port;

import com.sap.movies_service.categories.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryGatewayPort {
    Optional<Category> findById(UUID id);
    boolean existsById(UUID id);
    List<Category> findByIdIn(List<UUID> ids);
}
