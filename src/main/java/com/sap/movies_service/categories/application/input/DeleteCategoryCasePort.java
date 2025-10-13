package com.sap.movies_service.categories.application.input;

import java.util.UUID;

public interface DeleteCategoryCasePort {
    void deleteById(UUID id);
}
