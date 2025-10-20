

package com.sap.movies_service.classifications.application.usecases.createclassification;

import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.application.output.SaveClassificationPort;
import com.sap.movies_service.classifications.application.usecases.createclassification.dtos.CreateClassificationDTO;
import com.sap.movies_service.classifications.domain.Classification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClassificationCaseTest {

    @Mock
    private FindClassificationPort findClassificationPort;

    @Mock
    private SaveClassificationPort saveClassificationPort;

    @InjectMocks
    private CreateClassificationCase useCase;

    private static final String NAME_PG13 = "PG-13";
    private static final String DESC_GENERAL = "Apta para mayores de 13 años";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe crear y guardar la clasificación cuando el nombre no existe")
    void create_shouldSave_whenNameNotExists() {
        // Arrange
        when(findClassificationPort.findByNameIgnoreCase(NAME_PG13)).thenReturn(Optional.empty());
        when(saveClassificationPort.save(any(Classification.class))).thenAnswer(inv -> inv.getArgument(0));
        CreateClassificationDTO dto = new CreateClassificationDTO(NAME_PG13, DESC_GENERAL);

        // Act
        Classification result = useCase.create(dto);

        // Assert
        verify(findClassificationPort, times(1)).findByNameIgnoreCase(NAME_PG13);
        ArgumentCaptor<Classification> captor = ArgumentCaptor.forClass(Classification.class);
        verify(saveClassificationPort, times(1)).save(captor.capture());
        Classification saved = captor.getValue();
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(NAME_PG13);
        assertThat(saved.getDescription()).isEqualTo(DESC_GENERAL);
        assertThat(result.getName()).isEqualTo(NAME_PG13);
        assertThat(result.getDescription()).isEqualTo(DESC_GENERAL);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando la clasificación ya existe")
    void create_shouldThrow_whenNameAlreadyExists() {
        // Arrange
        when(findClassificationPort.findByNameIgnoreCase(NAME_PG13))
                .thenReturn(Optional.of(new Classification(NAME_PG13, DESC_GENERAL)));
        CreateClassificationDTO dto = new CreateClassificationDTO(NAME_PG13, DESC_GENERAL);

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto))
                .isInstanceOf(IllegalArgumentException.class);
        verify(saveClassificationPort, never()).save(any());
    }
}