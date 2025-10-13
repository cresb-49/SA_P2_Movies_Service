package com.sap.movies_service.movies.application.output;

import java.util.List;
import java.util.UUID;

public interface ModifyCategoriesMoviePort {
    int deleteByMovieIdAndCategoryIdIn(UUID movieId, List<UUID> categoryIds);

    int deleteByMovieId(UUID movieId);
}
