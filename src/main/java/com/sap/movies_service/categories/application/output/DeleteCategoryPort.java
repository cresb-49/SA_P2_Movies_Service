package com.sap.movies_service.categories.application.output;

import java.util.UUID;

public interface DeleteCategoryPort {
    void deleteById(UUID id);
}
