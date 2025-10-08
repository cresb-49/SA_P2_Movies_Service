package com.sap.movies_service.movies.infrastructure.input.dtos;

import com.sap.movies_service.movies.application.usecases.creategenere.dtos.CreateGenereDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGenreRequestDTO {
    private String name;

    public CreateGenereDTO toDTO() {
        return new CreateGenereDTO(name);
    }
}
