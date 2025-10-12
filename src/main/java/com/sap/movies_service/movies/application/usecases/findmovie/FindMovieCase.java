package com.sap.movies_service.movies.application.usecases.findmovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
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

    @Override
    public Movie findById(String id) {
        return findingMoviePort.findById(UUID.fromString(id)).orElseThrow(
                () -> new NotFoundException("Movie with id " + id + " does not exist")
        );
    }

    @Override
    public Page<Movie> findByTitle(String title, int page) {
        return findingMoviePort.findLikeTitle(title,page);
    }

    @Override
    public Page<Movie> findByGenere(UUID genereId, int page) {
        return findingMoviePort.findByGenereId(genereId,page);
    }

    @Override
    public Page<Movie> findAll(int page) {
        return findingMoviePort.findAll(page);
    }

    @Override
    public List<Movie> findByIdsIn(List<UUID> ids) {
        return findingMoviePort.findByIdsIn(ids);
    }
}
