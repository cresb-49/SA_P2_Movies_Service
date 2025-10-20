package com.sap.movies_service.movies.infrastructure.output.jpa.specifications;

import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.CategoryMovieEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieEntitySpecsTest {

    private static final String TITLE = "Matrix";
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final UUID CAT1 = UUID.randomUUID();
    private static final UUID CAT2 = UUID.randomUUID();

    @Mock private Root<MovieEntity> root;
    @Mock private CriteriaQuery<?> query;
    @Mock private CriteriaBuilder cb;

    @Test
    @DisplayName("byFilter: solo título genera like(lower(title)) y and con un único predicado")
    void byFilter_onlyTitle_buildsLikePredicate() {
        // Arrange
        MovieFilter filter = mock(MovieFilter.class);
        when(filter.title()).thenReturn(TITLE);
        when(filter.active()).thenReturn(null);
        when(filter.classificationId()).thenReturn(null);
        when(filter.categoryIds()).thenReturn(null);

        Path<Object> titlePath = mock(Path.class);
        when(root.get("title")).thenReturn(titlePath);
        Expression<String> lowered = mock(Expression.class);
        when(cb.lower(any(Expression.class))).thenReturn(lowered);
        Predicate likePredicate = mock(Predicate.class);
        when(cb.like(lowered, "%" + TITLE.toLowerCase() + "%")).thenReturn(likePredicate);

        Specification<MovieEntity> spec = MovieEntitySpecs.byFilter(filter);

        // Act
        Predicate result = spec.toPredicate(root, query, cb);

        // Assert
        verify(cb, times(1)).like(lowered, "%" + TITLE.toLowerCase() + "%");
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("byFilter: activo y clasificación generan equal() y and de ambos")
    void byFilter_activeAndClassification_buildsEqualPredicates() {
        // Arrange
        MovieFilter filter = mock(MovieFilter.class);
        when(filter.title()).thenReturn(null);
        when(filter.active()).thenReturn(Boolean.TRUE);
        when(filter.classificationId()).thenReturn(CLASSIFICATION_ID);
        when(filter.categoryIds()).thenReturn(null);

        Path<Object> activePath = mock(Path.class);
        when(root.get("active")).thenReturn(activePath);
        Predicate activePredicate = mock(Predicate.class);
        when(cb.equal(activePath, true)).thenReturn(activePredicate);

        Path<Object> classPath = mock(Path.class);
        when(root.get("classificationId")).thenReturn(classPath);
        Predicate classPredicate = mock(Predicate.class);
        when(cb.equal(classPath, CLASSIFICATION_ID)).thenReturn(classPredicate);

        Predicate andPredicate = mock(Predicate.class);
        when(cb.and(any(Predicate.class), any(Predicate.class))).thenReturn(andPredicate);

        Specification<MovieEntity> spec = MovieEntitySpecs.byFilter(filter);

        // Act
        Predicate result = spec.toPredicate(root, query, cb);

        // Assert
        verify(cb, times(1)).equal(activePath, true);
        verify(cb, times(1)).equal(classPath, CLASSIFICATION_ID);
        verify(cb, times(1)).and(any(Predicate.class), any(Predicate.class));
        assertThat(result).isEqualTo(andPredicate);
    }

    @Test
    @DisplayName("byFilter: sin criterios retorna null (no predicate)")
    void byFilter_noCriteria_returnsNull() {
        // Arrange
        MovieFilter filter = mock(MovieFilter.class);
        when(filter.title()).thenReturn(null);
        when(filter.active()).thenReturn(null);
        when(filter.classificationId()).thenReturn(null);
        when(filter.categoryIds()).thenReturn(null);

        Specification<MovieEntity> spec = MovieEntitySpecs.byFilter(filter);

        // Act
        Predicate result = spec.toPredicate(root, query, cb);

        // Assert
        assertThat(result).isNull();
        verifyNoInteractions(cb);
    }
}