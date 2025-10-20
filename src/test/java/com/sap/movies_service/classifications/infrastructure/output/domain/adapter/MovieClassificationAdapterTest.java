

package com.sap.movies_service.classifications.infrastructure.output.domain.adapter;

import com.sap.movies_service.movies.infrastructure.input.domain.port.MovieGatewayPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieClassificationAdapterTest {

    @Mock
    private MovieGatewayPort movieGatewayPort;

    @InjectMocks
    private MovieClassificationAdapter adapter;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe devolver true cuando existen películas asociadas a la clasificación")
    void hasMoviesWithClassificationId_shouldReturnTrue_whenMoviesExist() {
        // Arrange
        when(movieGatewayPort.hasMoviesWithClassificationId(CLASSIFICATION_ID)).thenReturn(true);
        // Act
        boolean result = adapter.hasMoviesWithClassificationId(CLASSIFICATION_ID);
        // Assert
        verify(movieGatewayPort, times(1)).hasMoviesWithClassificationId(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe devolver false cuando no existen películas asociadas a la clasificación")
    void hasMoviesWithClassificationId_shouldReturnFalse_whenNoMoviesExist() {
        // Arrange
        when(movieGatewayPort.hasMoviesWithClassificationId(CLASSIFICATION_ID)).thenReturn(false);
        // Act
        boolean result = adapter.hasMoviesWithClassificationId(CLASSIFICATION_ID);
        // Assert
        verify(movieGatewayPort, times(1)).hasMoviesWithClassificationId(CLASSIFICATION_ID);
        assertThat(result).isFalse();
    }
}