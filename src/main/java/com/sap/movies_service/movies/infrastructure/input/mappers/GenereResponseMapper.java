package com.sap.movies_service.movies.infrastructure.input.mappers;

import com.sap.movies_service.movies.domain.Genere;
import com.sap.movies_service.movies.infrastructure.input.dtos.GenereResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GenereResponseMapper {

    public GenereResponseDTO toResponse(Genere genere) {
        return new GenereResponseDTO(
                genere.getId(),
                genere.getName()
        );
    }

    public List<GenereResponseDTO> toResponseList(List<Genere> generes) {
        return generes.stream().map(this::toResponse).toList();
    }
}
