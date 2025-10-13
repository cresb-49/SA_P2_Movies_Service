package com.sap.movies_service.movies.application.output;

import java.util.List;
import java.util.UUID;

public interface FindingCategoriesPort {
    boolean existsById(UUID id);
    List<UUID> findAllById(List<UUID> ids);
}
