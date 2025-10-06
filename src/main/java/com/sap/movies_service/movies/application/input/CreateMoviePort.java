package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.application.usecases.createmovie.dtos.CreateMovieDTO;
import com.sap.movies_service.movies.domain.Movie;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;

@Validated
public interface CreateMoviePort {
    Movie create(@Valid CreateMovieDTO createMovieDTO) throws IOException;
}
