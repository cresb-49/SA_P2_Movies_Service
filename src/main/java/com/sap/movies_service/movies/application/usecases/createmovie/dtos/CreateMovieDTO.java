package com.sap.movies_service.movies.application.usecases.createmovie.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateMovieDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private Object image;
}
