package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.domain.Movie;

import java.util.UUID;

public interface ChangeStatePort {
    Movie changeState(UUID id);
}
