

package com.sap.movies_service.categories.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CategoryTest {

    private static final String NAME_ACCION = "Acción";
    private static final String NAME_DRAMA = "Drama";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe crear la categoría con nombre y timestamps válidos")
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange
        // Act
        Category category = new Category(NAME_ACCION);

        // Assert
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo(NAME_ACCION);
        assertThat(category.getCreatedAt()).isNotNull();
        assertThat(category.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("debe actualizar el nombre y la fecha de actualización")
    void update_shouldChangeNameAndUpdateTimestamp() {
        // Arrange
        Category category = new Category(NAME_ACCION);
        LocalDateTime previousUpdatedAt = category.getUpdatedAt();

        // Act
        category.update(NAME_DRAMA);

        // Assert
        assertThat(category.getName()).isEqualTo(NAME_DRAMA);
        assertThat(category.getUpdatedAt()).isAfter(previousUpdatedAt);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el nombre es nulo")
    void validate_shouldThrow_whenNameIsNull() {
        // Arrange
        Category category = new Category(NAME_ACCION);
        category.update(null);

        // Act & Assert
        assertThatThrownBy(category::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el nombre está vacío")
    void validate_shouldThrow_whenNameIsEmpty() {
        // Arrange
        Category category = new Category(NAME_ACCION);
        category.update("");

        // Act & Assert
        assertThatThrownBy(category::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe pasar la validación cuando el nombre es válido")
    void validate_shouldPass_whenNameIsValid() {
        // Arrange
        Category category = new Category(NAME_ACCION);

        // Act
        category.validate();

        // Assert
        assertThat(category.getName()).isEqualTo(NAME_ACCION);
    }
}