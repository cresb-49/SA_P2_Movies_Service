package com.sap.movies_service.movies.infrastructure.output.jpa.mapper;

import org.springframework.stereotype.Component;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MovieMapper {
    
    public Movie toDomain(MovieEntity entity){
        return new Movie(
            entity.getId(),
            entity.getTitle(),
            entity.getGenereId(),
            entity.getDuration(),
            entity.getSinopsis(),
            entity.getUrlImage()
        );
    }

    public MovieEntity toEntity(Movie movie){
        return new MovieEntity(
            movie.getId(),
            movie.getTitle(),
            movie.getGenereId(),
            movie.getDuration(),
            movie.getSinopsis(),
            movie.getUrlImage()
        );
    }
    
}
