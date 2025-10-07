package com.sap.movies_service.movies.infrastructure.input.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class MovieResponseDTO {
    private UUID id;
    private String title;
    private GenereResponseDTO genere;
    private int duration;
    private String sinopsis;
    private String urlImage;
}
