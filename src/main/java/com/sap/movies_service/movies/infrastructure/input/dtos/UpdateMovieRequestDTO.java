package com.sap.movies_service.movies.infrastructure.input.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateMovieRequestDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private Object image;
}
