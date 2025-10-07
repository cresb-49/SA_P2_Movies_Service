package com.sap.movies_service.movies.application.output;

import java.util.UUID;

public interface DeletingGenerePort {
    void deleteById(UUID id);
}
