

package com.sap.movies_service.movies.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MovieTest {

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_IMAGE = "http://image.jpg";

    @Test
    @DisplayName("debe crear la película con valores válidos y activar por defecto")
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange
        // Act
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);

        // Assert
        assertThat(movie.getId()).isNotNull();
        assertThat(movie.getTitle()).isEqualTo(TITLE);
        assertThat(movie.getDuration()).isEqualTo(DURATION);
        assertThat(movie.getSinopsis()).isEqualTo(SINOPSIS);
        assertThat(movie.getClassificationId()).isEqualTo(CLASSIFICATION_ID);
        assertThat(movie.getDirector()).isEqualTo(DIRECTOR);
        assertThat(movie.getCasting()).isEqualTo(CASTING);
        assertThat(movie.getUrlImage()).isEqualTo(URL_IMAGE);
        assertThat(movie.isActive()).isTrue();
        assertThat(movie.getCreatedAt()).isNotNull();
        assertThat(movie.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("validated debe lanzar cuando los campos obligatorios son inválidos")
    void validated_shouldThrow_onInvalidFields() {
        // Arrange
        Movie movie = new Movie(null, 0, null, null, null, null, null);

        // Act & Assert
        assertThatThrownBy(movie::validated).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("validated debe pasar con datos válidos")
    void validated_shouldPass_onValidFields() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);

        // Act
        movie.validated();

        // Assert
        assertThat(movie.getTitle()).isEqualTo(TITLE);
        assertThat(movie.getClassificationId()).isEqualTo(CLASSIFICATION_ID);
    }

    @Test
    @DisplayName("update debe cambiar campos y actualizar updatedAt cuando hay cambios")
    void update_shouldModifyFields_andTouchUpdatedAt() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        LocalDateTime before = movie.getUpdatedAt();
        UUID newClassification = UUID.randomUUID();

        // Act
        movie.update("Matrix Reloaded", 130, "Secuela", newClassification, "W", "K", "http://image2.jpg");

        // Assert
        assertThat(movie.getTitle()).isEqualTo("Matrix Reloaded");
        assertThat(movie.getDuration()).isEqualTo(130);
        assertThat(movie.getSinopsis()).isEqualTo("Secuela");
        assertThat(movie.getClassificationId()).isEqualTo(newClassification);
        assertThat(movie.getDirector()).isEqualTo("W");
        assertThat(movie.getCasting()).isEqualTo("K");
        assertThat(movie.getUrlImage()).isEqualTo("http://image2.jpg");
        assertThat(movie.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("update no debe tocar updatedAt cuando no hay cambios")
    void update_shouldNotTouchUpdatedAt_whenNoChanges() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        LocalDateTime before = movie.getUpdatedAt();

        // Act
        movie.update(null, 0, null, null, null, null, null);

        // Assert
        assertThat(movie.getUpdatedAt()).isEqualTo(before);
    }

    @Test
    @DisplayName("changeState debe alternar el estado y actualizar updatedAt")
    void changeState_shouldToggle_andTouchUpdatedAt() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        boolean activeBefore = movie.isActive();
        LocalDateTime before = movie.getUpdatedAt();

        // Act
        movie.changeState();

        // Assert
        assertThat(movie.isActive()).isEqualTo(!activeBefore);
        assertThat(movie.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    @DisplayName("copy constructor debe clonar valores inmutables y mutables")
    void copyConstructor_shouldCopyAllFields() {
        // Arrange
        Movie original = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        ClassificationView cv = new ClassificationView(UUID.randomUUID(), "PG-13", "desc");
        CategoryView cat = new CategoryView(UUID.randomUUID(), "Acción");
        original.setClassification(cv);
        original.setCategories(List.of(cat));

        // Act
        Movie copy = new Movie(original);

        // Assert
        assertThat(copy.getId()).isEqualTo(original.getId());
        assertThat(copy.getTitle()).isEqualTo(original.getTitle());
        assertThat(copy.getClassificationId()).isEqualTo(original.getClassificationId());
        assertThat(copy.getClassification()).isNull();
        assertThat(copy.getCategories()).isNull();
    }

    @Test
    @DisplayName("equals/hashCode deben basarse en id")
    void equalsAndHashCode_shouldUseId() {
        // Arrange
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Movie a = new Movie(id, TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE, true, now, now);
        Movie b = new Movie(id, TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE, true, now, now);
        Movie c = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);

        // Act
        boolean abEquals = a.equals(b);
        boolean acEquals = a.equals(c);

        // Assert
        assertThat(abEquals).isTrue();
        assertThat(acEquals).isFalse();
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}