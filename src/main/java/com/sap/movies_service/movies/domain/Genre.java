package com.sap.movies_service.movies.domain;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Genre {
    private UUID id;
    private String name;

    public Genre(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
