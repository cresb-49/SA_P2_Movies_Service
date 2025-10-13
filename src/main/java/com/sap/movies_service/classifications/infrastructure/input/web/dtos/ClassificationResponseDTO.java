package com.sap.movies_service.classifications.infrastructure.input.web.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClassificationResponseDTO(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt
) {
}
