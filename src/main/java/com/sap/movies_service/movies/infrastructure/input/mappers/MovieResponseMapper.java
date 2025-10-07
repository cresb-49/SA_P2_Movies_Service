package com.sap.movies_service.movies.infrastructure.input.mappers;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.input.dtos.MovieResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MovieResponseMapper {

    private final GenereResponseMapper genereResponseMapper;

    public MovieResponseDTO toResponse(Movie movie) {
        return new MovieResponseDTO(
                movie.getId(),
                movie.getTitle(),
                null,
                movie.getDuration(),
                movie.getSinopsis(),
                movie.getUrlImage()
        );
    }

    public List<MovieResponseDTO> toResponseList(List<Movie> movies) {
        return movies.stream().map(this::toResponse).toList();
    }
}
