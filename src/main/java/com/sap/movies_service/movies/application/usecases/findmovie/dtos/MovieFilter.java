package com.sap.movies_service.movies.application.usecases.findmovie.dtos;

import java.util.List;
import java.util.UUID;

public record MovieFilter(
        String name,
        Boolean active,
        UUID classificationId,
        List<UUID> categoryIds
) {
}
