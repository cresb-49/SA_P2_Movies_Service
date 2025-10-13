package com.sap.movies_service.classifications.infrastructure.input.web.dtos;

import com.sap.movies_service.classifications.application.usecases.createclassification.dtos.CreateClassificationDTO;

public record CreateClassificationRequestDTO(
        String name,
        String description
) {

    public CreateClassificationDTO toDTO() {
        return new CreateClassificationDTO(name, description);
    }
}
