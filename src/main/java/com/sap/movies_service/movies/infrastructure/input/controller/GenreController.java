package com.sap.movies_service.movies.infrastructure.input.controller;


import com.sap.movies_service.movies.application.input.CreateGenerePort;
import com.sap.movies_service.movies.application.input.DeleteGenerePort;
import com.sap.movies_service.movies.application.input.FindGenerePort;
import com.sap.movies_service.movies.infrastructure.input.dtos.CreateGenreRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.mappers.GenereResponseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
@AllArgsConstructor
@Tag(name = "Genre", description = "Movie API")
public class GenreController {

    private final CreateGenerePort createGenerePort;
    private final DeleteGenerePort deleteGenerePort;
    private final FindGenerePort findGenerePort;

    private final GenereResponseMapper genereResponseMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CINEMA_ADMIN')")
    public ResponseEntity<?> createGenre(
            @RequestBody CreateGenreRequestDTO createGenreRequestDTO
    ) {
        var result = createGenerePort.create(createGenreRequestDTO.toDTO());
        return ResponseEntity.ok(genereResponseMapper.toResponse(result));
    }

    //Public endpoints
    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        var result = findGenerePort.findAll();
        return ResponseEntity.ok(genereResponseMapper.toResponseList(result));
    }

    //public endpoint
    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable UUID id) {
        var result = findGenerePort.findById(id);
        return ResponseEntity.ok(genereResponseMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CINEMA_ADMIN')")
    public ResponseEntity<?> deleteGenre(@PathVariable UUID id) {
        deleteGenerePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
