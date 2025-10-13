package com.sap.movies_service.movies.domain;

import java.util.UUID;

public record ClassificationView(
        UUID id,
        String name,
        String description
) {
}
