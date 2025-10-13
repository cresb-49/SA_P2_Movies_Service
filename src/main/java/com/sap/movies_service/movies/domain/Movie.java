package com.sap.movies_service.movies.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Movie {
    private final UUID id;
    private String title;
    private int duration;
    private String sinopsis;
    private UUID classificationId;
    private String director;
    private String casting;
    private String urlImage;
    private boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //Setters for Relations View Domain Elements
    //Relations View Domain Elements
    @Setter
    private ClassificationView classification;
    @Setter
    private List<CategoryView> categories;

    public Movie(UUID id, String title, int duration, String sinopsis, UUID classificationId, String director,
                 String casting, String urlImage, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.sinopsis = sinopsis;
        this.classificationId = classificationId;
        this.director = director;
        this.casting = casting;
        this.active = active;
        this.urlImage = urlImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Movie(Movie movie) {
        this.id = movie.id;
        this.title = movie.title;
        this.duration = movie.duration;
        this.sinopsis = movie.sinopsis;
        this.classificationId = movie.classificationId;
        this.director = movie.director;
        this.casting = movie.casting;
        this.active = movie.active;
        this.urlImage = movie.urlImage;
        this.createdAt = movie.createdAt;
        this.updatedAt = movie.updatedAt;
    }

    public Movie(
            String title,
            int duration,
            String sinopsis,
            UUID classificationId,
            List<UUID> categoryIds,
            String director,
            String casting,
            String urlImage
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.duration = duration;
        this.sinopsis = sinopsis;
        this.classificationId = classificationId;
        this.director = director;
        this.casting = casting;
        this.active = true;
        this.urlImage = urlImage;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void validated() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (this.duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        if (this.sinopsis == null || this.sinopsis.isEmpty()) {
            throw new IllegalArgumentException("Sinopsis is required");
        }
        if (this.classificationId == null) {
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

    public void update(String title, int duration, String sinopsis, UUID classificationId, String director, String casting, String urlImage) {
        var updateFlag = false;
        if (title != null && !title.isEmpty()) {
            this.title = title;
            updateFlag = true;
        }
        if (duration > 0) {
            this.duration = duration;
            updateFlag = true;
        }
        if (sinopsis != null && !sinopsis.isEmpty()) {
            this.sinopsis = sinopsis;
            updateFlag = true;
        }
        if (classificationId != null) {
            this.classificationId = classificationId;
            updateFlag = true;
        }
        if (director != null && !director.isEmpty()) {
            this.director = director;
            updateFlag = true;
        }
        if (casting != null && !casting.isEmpty()) {
            this.casting = casting;
            updateFlag = true;
        }
        if (urlImage != null && !urlImage.isEmpty()) {
            this.urlImage = urlImage;
            updateFlag = true;
        }
        if (updateFlag) {
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void changeState() {
        this.active = !this.active;
        this.updatedAt = LocalDateTime.now();
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
