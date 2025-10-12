package com.sap.movies_service.movies.infrastructure.input.mappers;

import com.sap.movies_service.movies.domain.Genre;
import com.sap.common_lib.dto.response.movie.GenereResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GenereResponseMapper {

    public GenereResponseDTO toResponse(Genre genre) {
        return new GenereResponseDTO(
                genre.getId(),
                genre.getName()
        );
    }

    public List<GenereResponseDTO> toResponseList(List<Genre> genres) {
        return genres.stream().map(this::toResponse).toList();
    }
}
