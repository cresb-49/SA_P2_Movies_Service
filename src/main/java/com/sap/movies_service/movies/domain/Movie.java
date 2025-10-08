package com.sap.movies_service.movies.domain;

import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Movie {
    private UUID id;
    private String title;
    private Genre genre;
    private int duration;
    private String sinopsis;
    private String urlImage;

    public Movie(String title, Genre genre, int duration, String sinopsis) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.sinopsis = sinopsis;
    }

    public void validated() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (this.genre == null) {
            throw new IllegalArgumentException("Genere is required");
        }
        if (this.duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        if (this.sinopsis == null || this.sinopsis.isEmpty()) {
            throw new IllegalArgumentException("Sinopsis is required");
        }
        if (this.urlImage == null || this.urlImage.isEmpty()) {
            throw new IllegalArgumentException("URL Image is required");
        }
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
