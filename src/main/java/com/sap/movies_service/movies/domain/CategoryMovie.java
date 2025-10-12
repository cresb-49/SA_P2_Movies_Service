package com.sap.movies_service.movies.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CategoryMovie {

    private UUID id;
    private UUID categoryId;
    private UUID movieId;

    public CategoryMovie(UUID categoryId, UUID movieId) {
        this.id = UUID.randomUUID();
        this.categoryId = categoryId;
        this.movieId = movieId;
    }
}
