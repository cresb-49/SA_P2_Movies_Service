package com.sap.movies_service.classifications.application.output;

import java.util.UUID;

public interface DeleteClassificationPort {
    void deleteById(UUID id);
}
