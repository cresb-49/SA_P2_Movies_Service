package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Genere;

public interface SaveGenerePort {
    Genere save(Genere genere);
}
