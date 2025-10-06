package com.sap.movies_service.movies.application.usecases.createmovie.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateMovieDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private MultipartFile image;
}
