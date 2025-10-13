package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.CategoryView;

import java.util.List;
import java.util.UUID;

public interface FindingCategoriesPort {
    boolean existsById(UUID id);
    List<CategoryView> findAllById(List<UUID> ids);
}
