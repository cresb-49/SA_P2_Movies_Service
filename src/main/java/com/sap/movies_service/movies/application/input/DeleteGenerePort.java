package com.sap.movies_service.movies.application.input;

import java.util.UUID;

public interface DeleteGenerePort {
    void delete(UUID id);
}
