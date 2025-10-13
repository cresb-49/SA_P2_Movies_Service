package com.sap.movies_service.categories.infrastructure.input.web.dtos;

import com.sap.movies_service.categories.application.usecases.updatecategorycase.dtos.UpdateCategoryDTO;

import java.util.UUID;

public record UpdateCategoryRequestDTO(
        String name
) {

    public UpdateCategoryDTO toDTO(UUID id) {
        return new UpdateCategoryDTO(id, name);
    }
}
