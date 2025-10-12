package com.sap.movies_service.categories.application.output;

import com.sap.movies_service.categories.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindCategoryPort {
    Optional<Category> findById(UUID id);
    Optional<Category> findByNameInsensitive(String name);
    List<Category> findInIds(List<UUID> ids);
}
