package com.sap.movies_service.movies.application.usecases.findmovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FindMovieCase implements FindMoviePort {

    private final FindingMoviePort findingMoviePort;
    private final MovieFactory movieFactory;

    @Override
    public Movie findById(String id) {
        var movie = findingMoviePort.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Movie with id " + id + " does not exist")
        );
        return movieFactory.movieWithAllRelations(movie);
    }

    @Override
    public Page<Movie> findByTitle(String title, int page) {

        var result = findingMoviePort.findLikeTitle(title, page);
        return movieFactory.moviesWithAllRelations(result);
    }

    @Override
    public Page<Movie> findAll(int page) {

        var result = findingMoviePort.findAll(page);
        return movieFactory.moviesWithAllRelations(result);
    }

    @Override
    public Page<Movie> findByFilter(MovieFilter filter, int page) {
        var result = findingMoviePort.findByFilter(filter, page);
        return movieFactory.moviesWithAllRelations(result);
    }

    @Override
    public List<Movie> findByIdsIn(List<UUID> ids) {
        var result = findingMoviePort.findByIdsIn(ids);
        return movieFactory.moviesWithAllRelations(result);
    }
}
