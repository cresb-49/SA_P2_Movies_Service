package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.output.DeletingMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.MovieEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieJpaAdapter implements FindingMoviePort, SaveMoviePort, DeletingMoviePort {

    private final MovieEntityRepository movieEntityRepository;

    @Override
    public Optional<Movie> findById(UUID id) {
        var entity = movieEntityRepository.findById(id);
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        var entities = movieEntityRepository.findAll();
        return List.of();
    }

    @Override
    public List<Movie> findLikeTitle(String title) {
        var entities = movieEntityRepository.findByTitleContainingIgnoreCase(title);
        return List.of();
    }

    @Override
    public List<Movie> findByGenereId(UUID genereId) {
        var entities = movieEntityRepository.findByGenereId(genereId);
        return List.of();
    }

    @Override
    public Movie save(Movie movie) {
        var entity = movieEntityRepository.save(new MovieEntity());
        return null;
    }

    @Override
    public void deleteMovieById(UUID id) {
        movieEntityRepository.deleteById(id);
    }
}
