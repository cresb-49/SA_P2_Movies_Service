package com.sap.movies_service.movies.application.usecases.updatemovie.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateMovieDTO {
    private UUID id;
    private String title;
    private UUID classificationId;
    private List<UUID> categoryIds;
    private int duration;
    private String sinopsis;
    private String classification;
    private String director;
    private String casting;
    private MultipartFile image;
}
