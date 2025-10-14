package com.sap.movies_service.movies.infrastructure.input.web.mappers;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.common_lib.dto.response.movie.MovieResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MovieResponseMapper {

    private final CategoryViewResponseMapper categoryViewResponseMapper;
    private final ClassificationViewResponseMapper classificationViewResponseMapper;

    public MovieResponseDTO toResponse(Movie movie) {
        return new MovieResponseDTO(
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
                movie.getUpdatedAt(),
                classificationViewResponseMapper.toResponse(movie.getClassification()),
                categoryViewResponseMapper.toResponseList(movie.getCategories())
        );
    }

    public List<MovieResponseDTO> toResponseList(List<Movie> movies) {
        return movies.stream().map(this::toResponse).toList();
    }

    public Page<MovieResponseDTO> toResponsePage(Page<Movie> movies) {
        return movies.map(this::toResponse);
    }
}
