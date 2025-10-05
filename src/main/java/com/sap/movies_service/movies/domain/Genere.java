package com.sap.movies_service.movies.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Genere {
    private UUID id;
    private String name;

    public Genere(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
