package com.sap.movies_service.categories.application.usecases.updatecategorycase.dtos;

import java.util.UUID;

public record UpdateCategoryDTO(
        UUID id,
        String name
) {
}
