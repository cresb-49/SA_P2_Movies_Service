package com.sap.movies_service.movies.infrastructure.output.jpa.mapper;

import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenreEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GenreMapper {
    public Genre toDomain(GenreEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Genre(
                entity.getId(),
                entity.getName());
    }

    public GenreEntity toEntity(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreEntity(
                genre.getId(),
                genre.getName());
    }
}
