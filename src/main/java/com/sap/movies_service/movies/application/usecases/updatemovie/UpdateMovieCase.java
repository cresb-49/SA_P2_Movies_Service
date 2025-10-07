package com.sap.movies_service.movies.application.usecases.updatemovie;

import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateMovieCase implements UpdateMoviePort {
    @Override
    public Movie update(UpdateMovieDTO updateMovieDTO) {
        return null;
    }
}
