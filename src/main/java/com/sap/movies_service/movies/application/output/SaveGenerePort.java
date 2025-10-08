package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Genre;

public interface SaveGenerePort {
    Genre save(Genre genre);
}
