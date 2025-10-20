

package com.sap.movies_service.movies.infrastructure.output.domain.adapter;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.input.domain.port.ClassificationGatewayPort;
import com.sap.movies_service.movies.domain.ClassificationView;
import com.sap.movies_service.movies.infrastructure.output.domain.mapper.ClassificationViewMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationMovieAdapterTest {

    @Mock
    private ClassificationGatewayPort classificationGatewayPort;

    @Mock
    private ClassificationViewMapper mapper;

    @InjectMocks
    private ClassificationMovieAdapter adapter;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String NAME_PG13 = "PG-13";
    private static final String DESC = "Apta para mayores de 13 años";

    @Test
    @DisplayName("existsById debe delegar en el gateway y devolver true")
    void existsById_shouldDelegateAndReturnTrue() {
        // Arrange
        when(classificationGatewayPort.existsById(CLASSIFICATION_ID)).thenReturn(true);

        // Act
        boolean result = adapter.existsById(CLASSIFICATION_ID);

        // Assert
        verify(classificationGatewayPort, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsById debe delegar en el gateway y devolver false")
    void existsById_shouldDelegateAndReturnFalse() {
        // Arrange
        when(classificationGatewayPort.existsById(CLASSIFICATION_ID)).thenReturn(false);

        // Act
        boolean result = adapter.existsById(CLASSIFICATION_ID);

        // Assert
        verify(classificationGatewayPort, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("findById debe mapear la clasificación encontrada a vista")
    void findById_shouldMapToView_whenFound() {
        // Arrange
        Classification classification = new Classification(NAME_PG13, DESC);
        ClassificationView expected = new ClassificationView(CLASSIFICATION_ID, NAME_PG13, DESC);
        when(classificationGatewayPort.findById(CLASSIFICATION_ID)).thenReturn(Optional.of(classification));
        when(mapper.toView(classification)).thenReturn(expected);

        // Act
        ClassificationView result = adapter.findById(CLASSIFICATION_ID);

        // Assert
        verify(classificationGatewayPort, times(1)).findById(CLASSIFICATION_ID);
        verify(mapper, times(1)).toView(classification);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("findById debe devolver null cuando no se encuentra y llamar al mapper con null")
    void findById_shouldReturnNull_whenNotFound() {
        // Arrange
        when(classificationGatewayPort.findById(CLASSIFICATION_ID)).thenReturn(Optional.empty());

        // Act
        ClassificationView result = adapter.findById(CLASSIFICATION_ID);

        // Assert
        verify(classificationGatewayPort, times(1)).findById(CLASSIFICATION_ID);
        verify(mapper, times(1)).toView(null);
        assertThat(result).isNull();
    }
}