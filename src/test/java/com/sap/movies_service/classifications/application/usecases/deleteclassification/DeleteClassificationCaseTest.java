

package com.sap.movies_service.classifications.application.usecases.deleteclassification;

import com.sap.movies_service.classifications.application.output.DeleteClassificationPort;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.application.output.MoviesWithClassificationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteClassificationCaseTest {

    @Mock
    private DeleteClassificationPort deleteClassificationPort;

    @Mock
    private FindClassificationPort findClassificationPort;

    @Mock
    private MoviesWithClassificationPort moviesWithClassificationPort;

    @InjectMocks
    private DeleteClassificationCase useCase;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe eliminar la clasificación cuando existe y no tiene películas asociadas")
    void deleteById_shouldDelete_whenExistsAndNoMovies() {
        // Arrange
        when(findClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(moviesWithClassificationPort.hasMoviesWithClassificationId(CLASSIFICATION_ID)).thenReturn(false);

        // Act
        useCase.deleteById(CLASSIFICATION_ID);

        // Assert
        verify(findClassificationPort, times(1)).existsById(CLASSIFICATION_ID);
        verify(moviesWithClassificationPort, times(1)).hasMoviesWithClassificationId(CLASSIFICATION_ID);
        verify(deleteClassificationPort, times(1)).deleteById(CLASSIFICATION_ID);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando la clasificación no existe")
    void deleteById_shouldThrow_whenClassificationNotExists() {
        // Arrange
        when(findClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> useCase.deleteById(CLASSIFICATION_ID))
                .isInstanceOf(IllegalArgumentException.class);
        verify(deleteClassificationPort, never()).deleteById(any());
    }

    @Test
    @DisplayName("debe lanzar IllegalStateException cuando la clasificación tiene películas asociadas")
    void deleteById_shouldThrow_whenClassificationHasMovies() {
        // Arrange
        when(findClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(moviesWithClassificationPort.hasMoviesWithClassificationId(CLASSIFICATION_ID)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> useCase.deleteById(CLASSIFICATION_ID))
                .isInstanceOf(IllegalStateException.class);
        verify(deleteClassificationPort, never()).deleteById(any());
    }
}