package com.sap.movies_service.movies.infrastructure.output.jpa.mapper;

import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMapper {

    private final GenreMapper genreMapper;

    public Movie toDomain(MovieEntity entity) {
        if (entity == null) return null;
        return new Movie(
                entity.getId(),
                entity.getTitle(),
                genreMapper.toDomain(entity.getGenre()),
                entity.getDuration(),
                entity.getSinopsis(),
                entity.getUrlImage()
        );
    }

    public MovieEntity toEntity(Movie movie) {
        if (movie == null) return null;
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                genreMapper.toEntity(movie.getGenre()),
                movie.getDuration(),
                movie.getSinopsis(),
                movie.getUrlImage()
        );
    }

}
