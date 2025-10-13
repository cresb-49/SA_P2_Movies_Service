package com.sap.movies_service.categories.application.input;

import com.sap.movies_service.categories.domain.Category;

import java.util.List;
import java.util.UUID;

public interface FindCategoryCasePort {
    boolean existsById(UUID id);
    Category findById(UUID id);
    List<Category> findAll();
    List<Category> findAllById(List<UUID> ids);
    List<Category> findByNameInsensitive(String name);
}
