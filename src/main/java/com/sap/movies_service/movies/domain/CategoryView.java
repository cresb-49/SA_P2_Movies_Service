package com.sap.movies_service.movies.domain;

import java.util.UUID;

public record CategoryView(
        UUID id,
        String name
) {
}
