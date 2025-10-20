package com.sap.movies_service.movies.application.usecases.changestate;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.input.ChangeStatePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ChangeStateCase implements ChangeStatePort {

    private final FindingMoviePort findingMoviePort;
    private final SaveMoviePort saveMoviePort;
    private final MovieFactory movieFactory;

    @Override
    public Movie changeState(UUID id) {
        // Find movie
        var movie = findingMoviePort.findById(id).orElseThrow(
                () -> new NotFoundException("La película con ID " + id + " no existe")
        );
        // Change state
        movie.changeState();
        // Save movie
        var savedMovie = saveMoviePort.save(movie);
        // Return movie with categories and classification
        return movieFactory.movieWithAllRelations(savedMovie);
    }
}
