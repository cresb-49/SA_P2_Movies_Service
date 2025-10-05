package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.application.usecases.creategenere.dtos.CreateGenereDTO;
import com.sap.movies_service.movies.domain.Genere;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface CreateGenerePort {
    Genere create(@Valid CreateGenereDTO createGenereDTO);
}
