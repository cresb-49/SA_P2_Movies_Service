package com.sap.movies_service.classifications.application.output;

import java.util.UUID;

public interface MoviesWithClassificationPort {
    boolean hasMoviesWithClassificationId(UUID id);
}
