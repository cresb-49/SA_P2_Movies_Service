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
            throw new IllegalArgumentException("Classification name cannot be null or empty");
        }
        if (this.description == null || this.description.isEmpty()) {
            throw new IllegalArgumentException("Classification description cannot be null or empty");
        }
    }

}
