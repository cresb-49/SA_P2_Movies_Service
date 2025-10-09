package com.sap.movies_service.movies.infrastructure.input.dtos;

import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UpdateMovieRequestDTO {
    private UUID id;
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private MultipartFile image;

    public UpdateMovieDTO toDTO(UUID id, MultipartFile image) {
        return new UpdateMovieDTO(id,title, genereId, duration, sinopsis, image);
    }
}
