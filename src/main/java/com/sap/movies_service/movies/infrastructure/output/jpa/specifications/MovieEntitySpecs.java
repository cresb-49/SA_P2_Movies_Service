package com.sap.movies_service.movies.infrastructure.output.jpa.specifications;

import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import org.springframework.data.jpa.domain.Specification;

public class MovieEntitySpecs {
    public static Specification<MovieEntity> byFilter(MovieFilter f) {
        return Specification.allOf(
                eqName(f.name()),
                eqActive(f.active()),
                eqClassification(f.classificationId())
        );
    }

    private static Specification<MovieEntity> eqName(String name) {
        return (root, q, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    private static Specification<MovieEntity> eqActive(Boolean active) {
        return (root, q, cb) -> active == null ? null : cb.equal(root.get("active"), active);
    }
    private static Specification<MovieEntity> eqClassification(java.util.UUID classificationId) {
        return (root, q, cb) -> classificationId == null ? null : cb.equal(root.get("classification").get("id"), classificationId);
    }
}
