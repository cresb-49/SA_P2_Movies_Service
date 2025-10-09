package com.sap.movies_service.movies.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Movie {
    private UUID id;
    private String title;
    private Genre genre;
    private int duration;
    private String sinopsis;
    private String classification;
    private String director;
    private String casting;
    private String urlImage;

    public Movie(
            String title,
            Genre genre,
            int duration,
            String sinopsis,
            String classification,
            String director,
            String casting
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.sinopsis = sinopsis;
        this.classification = classification;
        this.director = director;
        this.casting = casting;
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
        if (this.classification == null || this.classification.isEmpty()) {
            throw new IllegalArgumentException("Classification is required");
        }
        if (this.director == null || this.director.isEmpty()) {
            throw new IllegalArgumentException("Director is required");
        }
        if (this.casting == null || this.casting.isEmpty()) {
            throw new IllegalArgumentException("Casting is required");
        }
        if (this.urlImage == null || this.urlImage.isEmpty()) {
            throw new IllegalArgumentException("URL Image is required");
        }
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Movie update(String title, Genre genre, int duration, String sinopsis, String classification, String director, String casting) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
        if (genre != null) {
            this.genre = genre;
        }
        if (duration > 0) {
            this.duration = duration;
        }
        if (sinopsis != null && !sinopsis.isEmpty()) {
            this.sinopsis = sinopsis;
        }
        if (classification != null && !classification.isEmpty()) {
            this.classification = classification;
        }
        if (director != null && !director.isEmpty()) {
            this.director = director;
        }
        if (casting != null && !casting.isEmpty()) {
            this.casting = casting;
        }
        return this;
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
