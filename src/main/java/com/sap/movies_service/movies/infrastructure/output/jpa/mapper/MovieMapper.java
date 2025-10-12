package com.sap.movies_service.movies.infrastructure.output.jpa.mapper;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMapper {

    public Movie toDomain(MovieEntity entity) {
        if (entity == null) return null;
        return new Movie(
                entity.getId(),
                entity.getTitle(),
                entity.getDuration(),
                entity.getSinopsis(),
                entity.getClassificationId(),
                entity.getDirector(),
                entity.getCasting(),
                entity.getUrlImage(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public MovieEntity toEntity(Movie movie) {
        if (movie == null) return null;
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getDuration(),
                movie.getSinopsis(),
                movie.getClassificationId(),
                movie.getDirector(),
                movie.getCasting(),
                movie.getUrlImage(),
                movie.isActive(),
                movie.getCreatedAt(),
                movie.getUpdatedAt()
        );
    }

}
