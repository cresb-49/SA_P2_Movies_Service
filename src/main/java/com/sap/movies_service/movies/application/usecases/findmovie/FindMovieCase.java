package com.sap.movies_service.movies.application.usecases.findmovie;

import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FindMovieCase implements FindMoviePort {

    private final FindingMoviePort findingMoviePort;

    @Override
    public Movie findById(String id) {
        return findingMoviePort.findById(UUID.fromString(id)).orElse(null);
    }

    @Override
    public List<Movie> findByTitle(String title) {
        return findingMoviePort.findLikeTitle(title);
    }

    @Override
    public List<Movie> findByGenere(UUID genereId) {
        return findingMoviePort.findByGenereId(genereId);
    }

    @Override
    public List<Movie> findAll() {
        return findingMoviePort.findAll();
    }
}
