package com.sap.movies_service.movies.infrastructure.input.web.controller;

import com.sap.movies_service.movies.application.input.*;
import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.infrastructure.input.web.dtos.CreateMovieRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.web.dtos.UpdateMovieRequestDTO;
import com.sap.movies_service.movies.infrastructure.input.web.mappers.MovieResponseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "Películas", description = "API de Películas")
public class MovieController {

    private final CreateMoviePort createMoviePort;
    private final UpdateMoviePort updateMoviePort;
    private final DeleteMoviePort deleteMoviePort;
    private final FindMoviePort findMoviePort;
    private final ChangeStatePort changeStatePort;

    private final MovieResponseMapper movieResponseMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Crear película",
            description = "Crea una nueva película con metadatos e imagen (multipart/form-data).",
            security = { @SecurityRequirement(name = "bearerAuth") },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = CreateMovieRequestDTO.class),
                            encoding = {
                                    @Encoding(name = "image", contentType = "image/*")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Película creada", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "409", description = "Conflicto", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<?> createMovie(
            @ModelAttribute CreateMovieRequestDTO createMovieRequestDTO,
            @RequestPart("image") @Parameter(description = "Archivo de imagen/afiche de la película", required = true) MultipartFile image
    ) throws IOException {
        var result = createMoviePort.create(createMovieRequestDTO.toDTO(image));
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Actualizar película",
            description = "Actualiza parcialmente una película por ID; acepta metadatos e imagen opcional (multipart/form-data).",
            security = { @SecurityRequirement(name = "bearerAuth") },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = UpdateMovieRequestDTO.class),
                            encoding = {
                                    @Encoding(name = "image", contentType = "image/*")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Película actualizada", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    public ResponseEntity<?> updateMovie(
            @PathVariable UUID id,
            @ModelAttribute UpdateMovieRequestDTO updateMovieRequestDTO,
            @RequestPart(value = "image", required = false) @Parameter(description = "Imagen nueva opcional de la película", required = false) MultipartFile image
    ) throws IOException {
        var result = updateMoviePort.update(updateMovieRequestDTO.toDTO(id, image));
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    @Operation(
            summary = "Eliminar película",
            description = "Elimina una película por su ID.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Eliminada correctamente", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteMovie(@PathVariable UUID id) {
        deleteMoviePort.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Public endpoints
    @Operation(
            summary = "Obtener película por ID",
            description = "Devuelve una película por su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable String id) {
        var result = findMoviePort.findById(id);
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    //public endpoint to get all movies with pagination
    @Operation(
            summary = "Buscar películas (paginado)",
            description = "Filtra por título, activo, classificationId y categoryIds (UUIDs separados por comas).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/public/search")
    public ResponseEntity<?> getAllMovies(
            @Parameter(description = "Índice de página, inicia en 0") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Filtro por coincidencia en el título") @RequestParam(name = "title", required = false) String title,
            @Parameter(description = "Solo películas activas si es true; incluye inactivas si es false; todas si es null") @RequestParam(name = "active", required = false) Boolean active,
            @Parameter(description = "Filtro por UUID de clasificación") @RequestParam(name = "classificationId", required = false) UUID classificationId,
            @Parameter(description = "UUIDs de categorías separados por comas, p. ej. 'id1,id2'") @RequestParam(name = "categoryIds", required = false) String categoryIds
    ) {
        var ids = categoryIds != null && !categoryIds.isBlank() ?
                categoryIds.split(",") : null;
        List<UUID> resultIds = ids != null ?
                java.util.Arrays.stream(ids).map(UUID::fromString).toList() : null;
        var filer = new MovieFilter(title, active, classificationId, resultIds);
        var result = findMoviePort.findByFilter(filer, page);
        return ResponseEntity.ok(movieResponseMapper.toResponsePage(result));
    }

    @Operation(
            summary = "Listar todas las películas (paginado)",
            description = "Devuelve todas las películas con paginación.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/public/all")
    public ResponseEntity<?> getAllMoviesNoPagination(
            @Parameter(description = "Índice de página, inicia en 0") @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        var result = findMoviePort.findAll(page);
        return ResponseEntity.ok(movieResponseMapper.toResponsePage(result));
    }

    @Operation(
            summary = "Cambiar estado activo de la película",
            description = "Cambia el estado activo/inactivo de una película por ID.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado actualizado", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PatchMapping("/state/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeMovieState(@PathVariable UUID id) {
        var result = changeStatePort.changeState(id);
        return ResponseEntity.ok(movieResponseMapper.toResponse(result));
    }

    @Operation(
            summary = "Obtener películas por IDs",
            description = "Devuelve una lista de películas que coinciden con los UUIDs proporcionados.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UUID.class)))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/public/ids")
    public ResponseEntity<?> getMoviesByIds(@RequestBody List<UUID> ids) {
        var result = findMoviePort.findByIdsIn(ids);
        return ResponseEntity.ok(movieResponseMapper.toResponseList(result));
    }
}