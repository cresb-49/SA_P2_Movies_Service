package com.sap.movies_service.movies.application.output;

import java.util.List;
import java.util.UUID;

public interface FindingCategoriesMoviePort {
    List<UUID> findCategoryIdsByMovieId(UUID movieId);
}
