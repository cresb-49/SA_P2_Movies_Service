package com.sap.movies_service.classifications.infrastructure.input.web.controller;

import com.sap.movies_service.classifications.application.input.CreateClassificationCasePort;
import com.sap.movies_service.classifications.application.input.DeleteClassificationCasePort;
import com.sap.movies_service.classifications.application.input.FindClassificationCasePort;
import com.sap.movies_service.classifications.infrastructure.input.web.dtos.CreateClassificationRequestDTO;
import com.sap.movies_service.classifications.infrastructure.input.web.mapper.ClassificationResponseMapper;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/classifications")
@AllArgsConstructor
@Tag(name = "Clasificaciones", description = "API de Clasificaciones de películas")
public class ClassificationController {

    private final CreateClassificationCasePort createCategoryCasePort;
    private final DeleteClassificationCasePort deleteCategoryCasePort;
    private final FindClassificationCasePort findClassificationCasePort;

    private final ClassificationResponseMapper classificationResponseMapper;

    // public endpoints
    @Operation(
            summary = "Obtener clasificación por ID",
            description = "Devuelve una clasificación por su identificador UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var classification = findClassificationCasePort.findById(id);
        var response = classificationResponseMapper.toDTO(classification);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear clasificación",
            description = "Crea una nueva clasificación.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateClassificationRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Creado", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateClassificationRequestDTO request) {
        var classification = createCategoryCasePort.create(request.toDTO());
        var response = classificationResponseMapper.toDTO(classification);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Eliminar clasificación",
            description = "Elimina una clasificación por su UUID.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Eliminado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        deleteCategoryCasePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // public endpoints
    @Operation(
            summary = "Listar todas las clasificaciones",
            description = "Devuelve todas las clasificaciones disponibles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/public/all")
    public ResponseEntity<?> getAll() {
        var classifications = findClassificationCasePort.findAll();
        var response = classificationResponseMapper.toDTOList(classifications);
        return ResponseEntity.ok(response);
    }

    // public endpoints
    @Operation(
            summary = "Obtener clasificaciones por IDs",
            description = "Devuelve una lista de clasificaciones que coinciden con los UUIDs proporcionados.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UUID.class)))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/public/ids")
    public ResponseEntity<?> getAllByIds(@RequestBody List<UUID> ids) {
        var classifications = findClassificationCasePort.findAllById(ids);
        var response = classificationResponseMapper.toDTOList(classifications);
        return ResponseEntity.ok(response);
    }
}
