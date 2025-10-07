package com.sap.movies_service.movies.infrastructure.input.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GenereResponseDTO {
    private UUID id;
    private String name;
}
