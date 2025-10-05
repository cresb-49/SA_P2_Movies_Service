package com.sap.movies_service.movies.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Movie {
    private UUID id;
    private String title;
    private UUID genereId;
    private int duration;
    private String sinopsis;
    private String urlImage;

    public Movie(String title, UUID genereId, int duration, String sinopsis, String urlImage) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.genereId = genereId;
        this.duration = duration;
        this.sinopsis = sinopsis;
        this.urlImage = urlImage;
    }
}
