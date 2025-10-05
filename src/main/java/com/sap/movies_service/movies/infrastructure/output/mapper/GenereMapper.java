package com.sap.movies_service.movies.infrastructure.output.mapper;

import org.springframework.stereotype.Component;

import com.sap.movies_service.movies.domain.Genere;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenereEntity;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GenereMapper {
    public Genere toDomain(GenereEntity entity) {
        return new Genere(
                entity.getId(),
                entity.getName());
    }

    public GenereEntity toEntity(Genere genere) {
        return new GenereEntity(
                genere.getId(),
                genere.getName());
    }
}
