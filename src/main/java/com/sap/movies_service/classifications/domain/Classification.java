package com.sap.movies_service.classifications.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Classification {

    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;

    public Classification(String name, String description) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la clasificación no puede ser nulo o vacío");
        }
        if (this.description == null || this.description.isEmpty()) {
            throw new IllegalArgumentException("La descripción de la clasificación no puede ser nula o vacía");
        }
    }

}
