package com.sap.movies_service.movies.application.usecases.updatemovie.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateMovieDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private Object image;
}
