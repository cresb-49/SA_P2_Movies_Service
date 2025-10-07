package com.sap.movies_service.movies.infrastructure.input.controller;

import com.sap.movies_service.movies.application.input.CreateMoviePort;
import com.sap.movies_service.movies.application.input.DeleteMoviePort;
import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.infrastructure.input.dtos.CreateMovieRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.dtos.UpdateMovieRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
    ) throws IOException {
        var result = createMoviePort.create(createMovieRequestDTO.toDTO(image));
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateMovie(
            @PathVariable UUID id,
            @ModelAttribute UpdateMovieRequestDTO updateMovieRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        var result = updateMoviePort.update(updateMovieRequestDTO.toDTO(id, image));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        deleteMoviePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        var result = findMoviePort.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<?> getMoviesByTitle(@PathVariable String title) {
        var result = findMoviePort.findByTitle(title);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/genere/{genereId}")
    public ResponseEntity<?> getMoviesByGenere(@PathVariable UUID genereId) {
        var result = findMoviePort.findByGenere(genereId);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies() {
        var result = findMoviePort.findAll();
        return ResponseEntity.ok(result);
    }
}