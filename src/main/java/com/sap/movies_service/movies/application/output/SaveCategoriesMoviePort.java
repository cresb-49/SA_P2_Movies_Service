package com.sap.movies_service.movies.application.output;

import com.sap.movies_service.movies.domain.CategoryMovie;

import java.util.List;
import java.util.UUID;

public interface SaveCategoriesMoviePort {
    void saveCategoriesMovie(List<CategoryMovie> categoryMovies);
}
