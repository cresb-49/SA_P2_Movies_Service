package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.output.DeletingMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.output.SaveCategoriesMoviePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.mapper.MovieMapper;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.CategoryMovieEntityRepository;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.MovieEntityRepository;
import com.sap.movies_service.movies.infrastructure.output.jpa.specifications.MovieEntitySpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieJpaAdapter implements FindingMoviePort, SaveMoviePort, DeletingMoviePort {

    private final MovieEntityRepository movieEntityRepository;
    private final MovieMapper movieMapper;

    @Override
    public boolean existsById(UUID id) {
        return movieEntityRepository.existsById(id);
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return movieEntityRepository.findById(id).map(movieMapper::toDomain);
    }

    @Override
    public Page<Movie> findAll(int page) {
        var result = movieEntityRepository.findAll(
                PageRequest.of(page, 20)
        );
        return result.map(movieMapper::toDomain);
    }

    @Override
    public Page<Movie> findByFilter(MovieFilter filter, int page) {
        var spec = MovieEntitySpecs.byFilter(filter);
        var result = movieEntityRepository.findAll(spec, PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt")));
        return result.map(movieMapper::toDomain);
    }

    @Override
    public List<Movie> findByIdsIn(List<UUID> ids) {
        var entities = movieEntityRepository.findByIdIn(ids);
        return entities.stream().map(movieMapper::toDomain).toList();
    }

    @Override
    public boolean hasMoviesWithClassificationId(UUID id) {
        return movieEntityRepository.existsByClassificationId(id);
    }

    @Override
    public Movie save(Movie movie) {
        var entity = movieMapper.toEntity(movie);
        var savedEntity = movieEntityRepository.save(entity);
        return movieMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteMovieById(UUID id) {
        movieEntityRepository.deleteById(id);
    }
}
