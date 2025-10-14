package com.sap.movies_service.movies.infrastructure.output.jpa.specifications;

import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.CategoryMovieEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class MovieEntitySpecs {
    public static Specification<MovieEntity> byFilter(MovieFilter f) {
        return Specification.allOf(
                eqTitle(f.title()),
                eqActive(f.active()),
                eqClassification(f.classificationId()),
                hasAnyCategory(f.categoryIds())
        );
    }

    private static Specification<MovieEntity> eqTitle(String title) {
        return (root, q, cb) -> title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private static Specification<MovieEntity> eqActive(Boolean active) {
        return (root, q, cb) -> active == null ? null : cb.equal(root.get("active"), active);
    }

    private static Specification<MovieEntity> eqClassification(java.util.UUID classificationId) {
        return (root, q, cb) -> classificationId == null ? null : cb.equal(root.get("classificationId"), classificationId);
    }

    private static Specification<MovieEntity> hasAnyCategory(Collection<UUID> categoryIds) {
        return (root, q, cb) -> {
            if (categoryIds == null || categoryIds.isEmpty()) return null;

            var sub = q.subquery(UUID.class);
            var cm = sub.from(CategoryMovieEntity.class);
            sub.select(cm.get("movieId"))
                    .where(
                            cb.equal(cm.get("movieId"), root.get("id")),
                            cm.get("categoryId").in(categoryIds)
                    );
            return cb.exists(sub);
        };
    }
}
