

package com.sap.movies_service.categories.application.usecases.deletecategory;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.output.DeleteCategoryPort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryCaseTest {

    @Mock
    private FindCategoryPort findCategoryPort;

    @Mock
    private DeleteCategoryPort deleteCategoryPort;

    @InjectMocks
    private DeleteCategoryCase useCase;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String CATEGORY_NAME = "Acción";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe eliminar la categoría cuando existe")
    void deleteById_shouldDeleteCategory_whenExists() {
        // Arrange
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.of(new Category(CATEGORY_NAME)));

        // Act
        useCase.deleteById(CATEGORY_ID);

        // Assert
        verify(findCategoryPort, times(1)).findById(CATEGORY_ID);
        verify(deleteCategoryPort, times(1)).deleteById(CATEGORY_ID);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la categoría no existe")
    void deleteById_shouldThrowNotFound_whenNotExists() {
        // Arrange
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.deleteById(CATEGORY_ID))
                .isInstanceOf(NotFoundException.class);
        verify(deleteCategoryPort, never()).deleteById(any());
    }
}