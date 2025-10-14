package com.sap.movies_service.movies.infrastructure.input.web.controller;

import com.sap.movies_service.movies.application.input.CreateMoviePort;
import com.sap.movies_service.movies.application.input.DeleteMoviePort;
import com.sap.movies_service.movies.application.input.FindMoviePort;
import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.infrastructure.input.web.dtos.CreateMovieRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.web.dtos.UpdateMovieRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.web.mappers.MovieResponseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    private final MovieResponseMapper movieResponseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createMovie(
            @ModelAttribute CreateMovieRequestDTO createMovieRequestDTO,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        var result = createMoviePort.create(createMovieRequestDTO.toDTO(image));
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateMovie(
            @PathVariable UUID id,
            @ModelAttribute UpdateMovieRequestDTO updateMovieRequestDTO,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        var result = updateMoviePort.update(updateMovieRequestDTO.toDTO(id, image));
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        deleteMoviePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Public endpoints
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        var result = findMoviePort.findById(id);
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    //public endpoint to get all movies with pagination
    @GetMapping("/search")
    public ResponseEntity<?> getAllMovies(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "classificationId", required = false) UUID classificationId,
            @RequestParam(name = "categoryIds", required = false) String categoryIds // comma separated UUIDs
    ) {
        var ids = categoryIds != null && !categoryIds.isBlank() ?
                categoryIds.split(",") : null;
        List<UUID> resultIds = ids != null ?
                java.util.Arrays.stream(ids).map(UUID::fromString).toList() : null;
        var filer = new MovieFilter(title, active, classificationId, resultIds);
        var result = findMoviePort.findByFilter(filer, page);
        return ResponseEntity.ok(movieResponseMapper.toResponsePage(result));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllMoviesNoPagination(
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        var result = findMoviePort.findAll(page);
        return ResponseEntity.ok(movieResponseMapper.toResponsePage(result));
    }

    @PostMapping("/ids")
    public ResponseEntity<?> getMoviesByIds(@RequestBody List<UUID> ids) {
        var result = findMoviePort.findByIdsIn(ids);
        return ResponseEntity.ok(movieResponseMapper.toResponseList(result));
    }
}