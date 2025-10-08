package com.sap.movies_service.movies.infrastructure.input.controller;


import com.sap.movies_service.movies.application.input.CreateGenerePort;
import com.sap.movies_service.movies.application.input.DeleteGenerePort;
import com.sap.movies_service.movies.application.input.FindGenerePort;
import com.sap.movies_service.movies.infrastructure.input.dtos.CreateGenreRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createGenre(
            @RequestBody CreateGenreRequestDTO createGenreRequestDTO
    ) {
        var result = createGenerePort.create(createGenreRequestDTO.toDTO());
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        var result = findGenerePort.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable UUID id) {
        var result = findGenerePort.findById(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable UUID id) {
        deleteGenerePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
