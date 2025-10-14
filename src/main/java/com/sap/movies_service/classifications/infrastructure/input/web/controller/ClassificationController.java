package com.sap.movies_service.classifications.infrastructure.input.web.controller;

import com.sap.movies_service.categories.application.input.CreateCategoryCasePort;
import com.sap.movies_service.categories.application.input.DeleteCategoryCasePort;
import com.sap.movies_service.classifications.application.input.CreateClassificationCasePort;
import com.sap.movies_service.classifications.application.input.DeleteClassificationCasePort;
import com.sap.movies_service.classifications.application.input.FindClassificationCasePort;
import com.sap.movies_service.classifications.infrastructure.input.web.dtos.CreateClassificationRequestDTO;
import com.sap.movies_service.classifications.infrastructure.input.web.mapper.ClassificationResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/classifications")
@AllArgsConstructor
public class ClassificationController {

    private final CreateClassificationCasePort createCategoryCasePort;
    private final DeleteClassificationCasePort deleteCategoryCasePort;
    private final FindClassificationCasePort findClassificationCasePort;

    private final ClassificationResponseMapper classificationResponseMapper;

    // public endpoints
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var classification = findClassificationCasePort.findById(id);
        var response = classificationResponseMapper.toDTO(classification);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody CreateClassificationRequestDTO request) {
        var classification = createCategoryCasePort.create(request.toDTO());
        var response = classificationResponseMapper.toDTO(classification);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        deleteCategoryCasePort.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // public endpoints
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        var classifications = findClassificationCasePort.findAll();
        var response = classificationResponseMapper.toDTOList(classifications);
        return ResponseEntity.ok(response);
    }

    // public endpoints
    @PostMapping("/ids")
    public ResponseEntity<?> getAllByIds(@RequestBody List<UUID> ids) {
        var classifications = findClassificationCasePort.findAllById(ids);
        var response = classificationResponseMapper.toDTOList(classifications);
        return ResponseEntity.ok(response);
    }
}
