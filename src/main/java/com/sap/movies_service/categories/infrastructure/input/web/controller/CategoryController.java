package com.sap.movies_service.categories.infrastructure.input.web.controller;

import com.sap.movies_service.categories.application.input.CreateCategoryCasePort;
import com.sap.movies_service.categories.application.input.DeleteCategoryCasePort;
import com.sap.movies_service.categories.application.input.FindCategoryCasePort;
import com.sap.movies_service.categories.application.input.UpdateCategoryCasePort;
import com.sap.movies_service.categories.infrastructure.input.web.dtos.CreateCategoryRequestDTO;
import com.sap.movies_service.categories.infrastructure.input.web.dtos.UpdateCategoryRequestDTO;
import com.sap.movies_service.categories.infrastructure.input.web.mapper.CategoryResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private final CreateCategoryCasePort createCategoryCasePort;
    private final DeleteCategoryCasePort deleteCategoryCasePort;
    private final FindCategoryCasePort findCategoryCasePort;
    private final UpdateCategoryCasePort updateCategoryCasePort;

    private final CategoryResponseMapper categoryResponseMapper;

    @GetMapping("/public/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var category = findCategoryCasePort.findById(id);
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateCategoryRequestDTO request) {
        var category = createCategoryCasePort.create(request.name());
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.status(201).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id,@RequestBody UpdateCategoryRequestDTO request) {
        var category = updateCategoryCasePort.update(request.toDTO(id));
        var response = categoryResponseMapper.toDTO(category);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        deleteCategoryCasePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/public/ids")
    public ResponseEntity<?> getByIds(@RequestBody List<UUID> ids) {
        var categories = findCategoryCasePort.findAllById(ids);
        var response = categoryResponseMapper.toDTOList(categories);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/search")
    public ResponseEntity<?> search(@RequestParam(name = "query", required = false) String query) {
        var categories = findCategoryCasePort.findByNameInsensitive(query);
        var response = categoryResponseMapper.toDTOList(categories);
        return ResponseEntity.ok(response);
    }


}
