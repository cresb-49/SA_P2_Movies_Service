

package com.sap.movies_service.classifications.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ClassificationTest {

    private static final String NAME_PG13 = "PG-13";
    private static final String DESC_GENERAL = "Apta para mayores de 13 años";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe crear la clasificación con valores válidos")
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange
        // Act
        Classification classification = new Classification(NAME_PG13, DESC_GENERAL);

        // Assert
        assertThat(classification.getId()).isNotNull();
        assertThat(classification.getName()).isEqualTo(NAME_PG13);
        assertThat(classification.getDescription()).isEqualTo(DESC_GENERAL);
        assertThat(classification.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el nombre es nulo")
    void validate_shouldThrow_whenNameIsNull() {
        // Arrange
        Classification classification = new Classification(null, DESC_GENERAL);

        // Act & Assert
        assertThatThrownBy(classification::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el nombre está vacío")
    void validate_shouldThrow_whenNameIsEmpty() {
        // Arrange
        Classification classification = new Classification("", DESC_GENERAL);

        // Act & Assert
        assertThatThrownBy(classification::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando la descripción es nula")
    void validate_shouldThrow_whenDescriptionIsNull() {
        // Arrange
        Classification classification = new Classification(NAME_PG13, null);

        // Act & Assert
        assertThatThrownBy(classification::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando la descripción está vacía")
    void validate_shouldThrow_whenDescriptionIsEmpty() {
        // Arrange
        Classification classification = new Classification(NAME_PG13, "");

        // Act & Assert
        assertThatThrownBy(classification::validate)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe pasar la validación cuando los valores son válidos")
    void validate_shouldPass_whenValid() {
        // Arrange
        Classification classification = new Classification(NAME_PG13, DESC_GENERAL);

        // Act
        classification.validate();

        // Assert
        assertThat(classification.getName()).isEqualTo(NAME_PG13);
        assertThat(classification.getDescription()).isEqualTo(DESC_GENERAL);
    }
}