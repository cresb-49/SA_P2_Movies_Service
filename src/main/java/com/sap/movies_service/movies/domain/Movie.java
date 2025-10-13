package com.sap.movies_service.movies.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Movie {
    private UUID id;
    private String title;
    private int duration;
    private String sinopsis;
    private UUID classificationId;
    private List<UUID> categoryIds;
    private String director;
    private String casting;
    private String urlImage;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
        this.categoryIds = categoryIds;
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
        if (this.categoryIds == null || this.categoryIds.isEmpty()) {
            throw new IllegalArgumentException("At least one category is required");
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

    public void update(String title, int duration, String sinopsis, UUID classificationId, List<UUID> categoryIds, String director, String casting, String urlImage) {
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
        if (categoryIds != null && !categoryIds.isEmpty()) {
            this.categoryIds = categoryIds;
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
