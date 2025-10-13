package com.sap.movies_service.classifications.application.input;

import java.util.UUID;

public interface DeleteClassificationCasePort {
    void deleteById(UUID id);
}
