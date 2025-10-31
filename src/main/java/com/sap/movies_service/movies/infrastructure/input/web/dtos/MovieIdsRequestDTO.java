package com.sap.movies_service.movies.infrastructure.input.web.dtos;

import java.util.List;
import java.util.UUID;

public record MovieIdsRequestDTO(
        List<UUID> ids
) {
}