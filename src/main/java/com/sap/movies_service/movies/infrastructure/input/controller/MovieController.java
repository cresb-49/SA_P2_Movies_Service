package com.sap.movies_service.movies.infrastructure.input.controller;

import com.sap.movies_service.movies.application.input.CreateMoviePort;
import com.sap.movies_service.movies.application.input.DeleteMoviePort;
import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.infrastructure.input.dtos.CreateMovieRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/movies")
@AllArgsConstructor
@Tag(name = "Movie", description = "Movie API")
public class MovieController {

    private final CreateMoviePort createMoviePort;
    private final UpdateMoviePort updateMoviePort;
    private final DeleteMoviePort deleteMoviePort;
    private final FindMoviePort findMoviePort;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovie(
            @ModelAttribute CreateMovieRequestDTO createMovieRequestDTO,
            @RequestPart("image") MultipartFile image
    ) {
        var result = createMoviePort.create(createMovieRequestDTO.toDTO(image));
        return ResponseEntity.ok(result);
    }
}
