package com.sap.movies_service.movies.application.output;

import java.util.UUID;

public interface DeletingMoviePort {
    void deleteMovieById(UUID id);
}
