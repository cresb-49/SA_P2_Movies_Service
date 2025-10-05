package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.Movie;

public interface SaveMoviePort {
    Movie save(Movie movie);
}
