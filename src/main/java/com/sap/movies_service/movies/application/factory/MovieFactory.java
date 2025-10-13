package com.sap.movies_service.movies.application.factory;

import com.sap.movies_service.movies.application.output.FindingCategoriesMoviePort;
import com.sap.movies_service.movies.application.output.FindingCategoriesPort;
import com.sap.movies_service.movies.application.output.FindingClassificationPort;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MovieFactory {

    private final FindingClassificationPort findingClassificationPort;
    private final FindingCategoriesPort findingCategoriesPort;
    private final FindingCategoriesMoviePort findingCategoriesMoviePort;

    public Movie movieWithAllRelations(Movie movie) {
        Movie movieWithAllRelations = new Movie(movie);
        var classification = findingClassificationPort.findById(movie.getClassificationId());
        var categoriesIds = findingCategoriesMoviePort.findCategoryIdsByMovieId(movie.getId());
        var categories = findingCategoriesPort.findAllById(categoriesIds);
        // Set classification
        movieWithAllRelations.setClassification(classification);
        // Set categories
        movieWithAllRelations.setCategories(categories);
        // Return movie with all relations
        return movieWithAllRelations;
    }

    public Page<Movie> moviesWithAllRelations(Page<Movie> movies) {
        return movies.map(this::movieWithAllRelations);
    }

    public List<Movie> moviesWithAllRelations(List<Movie> movies) {
        return movies.stream().map(this::movieWithAllRelations).toList();
    }
}
