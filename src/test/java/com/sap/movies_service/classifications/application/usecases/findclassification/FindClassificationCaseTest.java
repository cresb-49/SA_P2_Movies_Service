

package com.sap.movies_service.classifications.application.usecases.findclassification;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.domain.Classification;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindClassificationCaseTest {

    @Mock
    private FindClassificationPort findClassificationPort;

    @InjectMocks
    private FindClassificationCase useCase;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String NAME_PG13 = "PG-13";
    private static final String DESC_GENERAL = "Apta para mayores de 13 años";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe devolver true cuando la clasificación existe")
    void existsById_shouldReturnTrue_whenExists() {
        // Arrange
        when(findClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);

        // Act
        boolean result = useCase.existsById(CLASSIFICATION_ID);

        // Assert
        verify(findClassificationPort, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe devolver false cuando la clasificación no existe")
    void existsById_shouldReturnFalse_whenNotExists() {
        // Arrange
        when(findClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(false);

        // Act
        boolean result = useCase.existsById(CLASSIFICATION_ID);

        // Assert
        verify(findClassificationPort, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe devolver la clasificación cuando existe por id")
    void findById_shouldReturnClassification_whenExists() {
        // Arrange
        Classification expected = new Classification(NAME_PG13, DESC_GENERAL);
        when(findClassificationPort.findById(CLASSIFICATION_ID)).thenReturn(Optional.of(expected));

        // Act
        Classification result = useCase.findById(CLASSIFICATION_ID);

        // Assert
        verify(findClassificationPort, times(1)).findById(CLASSIFICATION_ID);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando no existe por id")
    void findById_shouldThrow_whenNotExists() {
        // Arrange
        when(findClassificationPort.findById(CLASSIFICATION_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.findById(CLASSIFICATION_ID))
                .isInstanceOf(NotFoundException.class);
        verify(findClassificationPort, times(1)).findById(CLASSIFICATION_ID);
    }

    @Test
    @DisplayName("debe devolver todas las clasificaciones")
    void findAll_shouldReturnAll() {
        // Arrange
        List<Classification> expected = List.of(new Classification(NAME_PG13, DESC_GENERAL));
        when(findClassificationPort.findAll()).thenReturn(expected);

        // Act
        List<Classification> result = useCase.findAll();

        // Assert
        verify(findClassificationPort, times(1)).findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe devolver clasificaciones por lista de IDs")
    void findAllById_shouldReturnListByIds() {
        // Arrange
        List<UUID> ids = List.of(CLASSIFICATION_ID);
        List<Classification> expected = List.of(new Classification(NAME_PG13, DESC_GENERAL));
        when(findClassificationPort.findAllById(ids)).thenReturn(expected);

        // Act
        List<Classification> result = useCase.findAllById(ids);

        // Assert
        verify(findClassificationPort, times(1)).findAllById(ids);
        assertThat(result).isEqualTo(expected);
    }
}