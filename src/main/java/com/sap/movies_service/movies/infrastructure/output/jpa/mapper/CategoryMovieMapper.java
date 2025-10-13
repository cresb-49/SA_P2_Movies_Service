package com.sap.movies_service.movies.infrastructure.output.jpa.mapper;

import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.CategoryMovieEntity;

public class CategoryMovieMapper {
    public CategoryMovieEntity toEntity(CategoryMovie domain) {
        if (domain == null) return null;
        return new CategoryMovieEntity(
                domain.getId(),
                domain.getCategoryId(),
                domain.getMovieId()
        );
    }

    public CategoryMovie toDomain(CategoryMovie domain) {
        if (domain == null) return null;
        return new CategoryMovie(
                domain.getId(),
                domain.getCategoryId(),
                domain.getMovieId()
        );
    }
}
