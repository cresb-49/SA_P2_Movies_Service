package com.sap.movies_service.movies.infrastructure.input.web.dtos;

import com.sap.movies_service.movies.application.usecases.createmovie.dtos.CreateMovieDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class CreateMovieRequestDTO {
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private String classification;
    private String director;
    private String casting;

    public CreateMovieDTO toDTO(MultipartFile image) {
        return new CreateMovieDTO(title, genereId, duration, sinopsis, classification, director, casting, image);
    }
}
