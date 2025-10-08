package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.application.usecases.creategenere.dtos.CreateGenereDTO;
import com.sap.movies_service.movies.domain.Genre;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateGenerePort {
    Genre create(@Valid CreateGenereDTO createGenereDTO);
}
