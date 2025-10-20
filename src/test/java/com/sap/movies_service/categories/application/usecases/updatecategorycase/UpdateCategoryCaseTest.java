

package com.sap.movies_service.categories.application.usecases.updatecategorycase;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.application.output.SaveCategoryPort;
import com.sap.movies_service.categories.application.usecases.updatecategorycase.dtos.UpdateCategoryDTO;
import com.sap.movies_service.categories.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryCaseTest {

    @Mock
    private FindCategoryPort findCategoryPort;

    @Mock
    private SaveCategoryPort saveCategoryPort;

    @InjectMocks
    private UpdateCategoryCase useCase;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String NAME_ACCION = "Acción";
    private static final String NAME_DRAMA = "Drama";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe actualizar y guardar la categoría cuando existe y el nuevo nombre no está en uso")
    void update_shouldUpdateAndSaveCategory_whenValid() {
        // Arrange
        Category existing = new Category(NAME_ACCION);
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.of(existing));
        when(findCategoryPort.findByNameInsensitive(NAME_DRAMA, CATEGORY_ID)).thenReturn(Optional.empty());
        when(saveCategoryPort.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));
        UpdateCategoryDTO dto = new UpdateCategoryDTO(CATEGORY_ID, NAME_DRAMA);

        // Act
        useCase.update(dto);

        // Assert
        verify(findCategoryPort, times(1)).findById(CATEGORY_ID);
        verify(findCategoryPort, times(1)).findByNameInsensitive(NAME_DRAMA, CATEGORY_ID);
        verify(saveCategoryPort, times(1)).save(existing);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la categoría no existe")
    void update_shouldThrowNotFound_whenCategoryDoesNotExist() {
        // Arrange
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        UpdateCategoryDTO dto = new UpdateCategoryDTO(CATEGORY_ID, NAME_DRAMA);

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto))
                .isInstanceOf(NotFoundException.class);
        verify(saveCategoryPort, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el nuevo nombre ya existe en otra categoría")
    void update_shouldThrowIllegalArgument_whenNameAlreadyExists() {
        // Arrange
        Category existing = new Category(NAME_ACCION);
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.of(existing));
        when(findCategoryPort.findByNameInsensitive(NAME_DRAMA, CATEGORY_ID))
                .thenReturn(Optional.of(new Category(NAME_DRAMA)));
        UpdateCategoryDTO dto = new UpdateCategoryDTO(CATEGORY_ID, NAME_DRAMA);

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto))
                .isInstanceOf(IllegalArgumentException.class);
        verify(saveCategoryPort, never()).save(any());
    }
}