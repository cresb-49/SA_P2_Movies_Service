package com.sap.movies_service.movies.application.input;

import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.Movie;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UpdateMoviePort {
    Movie update(@Valid UpdateMovieDTO updateMovieDTO);
}
