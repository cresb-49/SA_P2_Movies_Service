package com.sap.movies_service.movies.application.usecases.updatemovie.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateMovieDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private Object image;
}
