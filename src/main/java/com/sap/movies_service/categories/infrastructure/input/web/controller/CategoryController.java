package com.sap.movies_service.categories.infrastructure.input.web.controller;

import com.sap.movies_service.categories.application.input.CreateCategoryCasePort;
import com.sap.movies_service.categories.application.input.DeleteCategoryCasePort;
import com.sap.movies_service.categories.application.input.FindCategoryCasePort;
import com.sap.movies_service.categories.application.input.UpdateCategoryCasePort;
import com.sap.movies_service.categories.infrastructure.input.web.dtos.CreateCategoryRequestDTO;
import com.sap.movies_service.categories.infrastructure.input.web.dtos.UpdateCategoryRequestDTO;
import com.sap.movies_service.categories.infrastructure.input.web.mapper.CategoryResponseMapper;
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
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
@Tag(name = "Categorías", description = "API de categorías de películas")
public class CategoryController {

    private final CreateCategoryCasePort createCategoryCasePort;
    private final DeleteCategoryCasePort deleteCategoryCasePort;
    private final FindCategoryCasePort findCategoryCasePort;
    private final UpdateCategoryCasePort updateCategoryCasePort;

    private final CategoryResponseMapper categoryResponseMapper;

    @Operation(
            summary = "Obtener categoría por ID",
            description = "Devuelve una categoría por su UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @GetMapping("/public/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var category = findCategoryCasePort.findById(id);
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear categoría",
            description = "Crea una nueva categoría.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCategoryRequestDTO.class))
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
    public ResponseEntity<?> create(@RequestBody CreateCategoryRequestDTO request) {
        var category = createCategoryCasePort.create(request.name());
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Actualizar categoría",
            description = "Actualiza parcialmente una categoría por su UUID.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCategoryRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Actualizado", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "403", description = "Prohibido", content = @Content(schema = @Schema(hidden = true))),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody UpdateCategoryRequestDTO request) {
        var category = updateCategoryCasePort.update(request.toDTO(id));
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría por su UUID.",
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

    @Operation(
            summary = "Obtener categorías por IDs",
            description = "Devuelve una lista de categorías que coinciden con los UUIDs proporcionados.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UUID.class)))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/public/ids")
    public ResponseEntity<?> getByIds(@RequestBody List<UUID> ids) {
        var categories = findCategoryCasePort.findAllById(ids);
        var response = categoryResponseMapper.toDTOList(categories);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar categorías",
            description = "Busca categorías por nombre con coincidencia insensible a mayúsculas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Éxito", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/public/search")
    public ResponseEntity<?> search(@io.swagger.v3.oas.annotations.Parameter(description = "Texto a buscar en el nombre de la categoría (coincidencia no sensible a mayúsculas)") @RequestParam(name = "query", required = false) String query) {
        var categories = findCategoryCasePort.findByNameInsensitive(query);
        var response = categoryResponseMapper.toDTOList(categories);
        return ResponseEntity.ok(response);
    }


}
