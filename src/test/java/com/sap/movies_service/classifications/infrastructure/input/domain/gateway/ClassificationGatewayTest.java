

package com.sap.movies_service.classifications.infrastructure.input.domain.gateway;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.output.jpa.adapter.ClassificationJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationGatewayTest {

    @Mock
    private ClassificationJpaAdapter classificationJpaAdapter;

    @InjectMocks
    private ClassificationGateway gateway;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String NAME_PG13 = "PG-13";
    private static final String DESC_GENERAL = "Apta para mayores de 13 años";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe indicar existencia por ID - true")
    void existsById_shouldReturnTrue() {
        // Arrange
        when(classificationJpaAdapter.existsById(CLASSIFICATION_ID)).thenReturn(true);
        // Act
        boolean result = gateway.existsById(CLASSIFICATION_ID);
        // Assert
        verify(classificationJpaAdapter, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe indicar existencia por ID - false")
    void existsById_shouldReturnFalse() {
        // Arrange
        when(classificationJpaAdapter.existsById(CLASSIFICATION_ID)).thenReturn(false);
        // Act
        boolean result = gateway.existsById(CLASSIFICATION_ID);
        // Assert
        verify(classificationJpaAdapter, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe devolver la clasificación cuando se encuentra por ID")
    void findById_shouldReturnClassification_whenExists() {
        // Arrange
        Classification classification = new Classification(NAME_PG13, DESC_GENERAL);
        when(classificationJpaAdapter.findById(CLASSIFICATION_ID)).thenReturn(Optional.of(classification));
        // Act
        Optional<Classification> result = gateway.findById(CLASSIFICATION_ID);
        // Assert
        verify(classificationJpaAdapter, times(1)).findById(CLASSIFICATION_ID);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(classification);
    }

    @Test
    @DisplayName("debe devolver Optional vacío cuando no se encuentra por ID")
    void findById_shouldReturnEmpty_whenNotExists() {
        // Arrange
        when(classificationJpaAdapter.findById(CLASSIFICATION_ID)).thenReturn(Optional.empty());
        // Act
        Optional<Classification> result = gateway.findById(CLASSIFICATION_ID);
        // Assert
        verify(classificationJpaAdapter, times(1)).findById(CLASSIFICATION_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe devolver lista de clasificaciones al buscar por IDs")
    void findByIds_shouldReturnList_whenIdsProvided() {
        // Arrange
        List<UUID> ids = List.of(CLASSIFICATION_ID);
        List<Classification> expected = List.of(new Classification(NAME_PG13, DESC_GENERAL));
        when(classificationJpaAdapter.findAllById(ids)).thenReturn(expected);
        // Act
        List<Classification> result = gateway.findByIds(ids);
        // Assert
        verify(classificationJpaAdapter, times(1)).findAllById(ids);
        assertThat(result).isEqualTo(expected);
    }
}