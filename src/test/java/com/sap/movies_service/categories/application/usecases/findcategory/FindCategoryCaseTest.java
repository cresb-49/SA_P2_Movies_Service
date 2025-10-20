

package com.sap.movies_service.categories.application.usecases.findcategory;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.domain.Category;
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
class FindCategoryCaseTest {

    @Mock
    private FindCategoryPort findCategoryPort;

    @InjectMocks
    private FindCategoryCase useCase;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String NAME_ACCION = "Acción";
    private static final String NAME_EMPTY = "";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe devolver true cuando la categoría existe")
    void existsById_shouldReturnTrue_whenExists() {
        // Arrange
        when(findCategoryPort.existsById(CATEGORY_ID)).thenReturn(true);

        // Act
        boolean result = useCase.existsById(CATEGORY_ID);

        // Assert
        verify(findCategoryPort, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe devolver false cuando la categoría no existe")
    void existsById_shouldReturnFalse_whenNotExists() {
        // Arrange
        when(findCategoryPort.existsById(CATEGORY_ID)).thenReturn(false);

        // Act
        boolean result = useCase.existsById(CATEGORY_ID);

        // Assert
        verify(findCategoryPort, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe devolver la categoría cuando existe por id")
    void findById_shouldReturnCategory_whenExists() {
        // Arrange
        Category expected = new Category(NAME_ACCION);
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.of(expected));

        // Act
        Category result = useCase.findById(CATEGORY_ID);

        // Assert
        verify(findCategoryPort, times(1)).findById(CATEGORY_ID);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando no existe por id")
    void findById_shouldThrow_whenNotExists() {
        // Arrange
        when(findCategoryPort.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.findById(CATEGORY_ID))
                .isInstanceOf(NotFoundException.class);
        verify(findCategoryPort, times(1)).findById(CATEGORY_ID);
    }

    @Test
    @DisplayName("debe devolver todas las categorías")
    void findAll_shouldReturnAllCategories() {
        // Arrange
        List<Category> expected = List.of(new Category(NAME_ACCION));
        when(findCategoryPort.findAll()).thenReturn(expected);

        // Act
        List<Category> result = useCase.findAll();

        // Assert
        verify(findCategoryPort, times(1)).findAll();
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe devolver categorías por lista de IDs")
    void findAllById_shouldReturnCategoriesByIds() {
        // Arrange
        List<UUID> ids = List.of(CATEGORY_ID);
        List<Category> expected = List.of(new Category(NAME_ACCION));
        when(findCategoryPort.findInIds(ids)).thenReturn(expected);

        // Act
        List<Category> result = useCase.findAllById(ids);

        // Assert
        verify(findCategoryPort, times(1)).findInIds(ids);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe devolver todas las categorías cuando el nombre es nulo o vacío")
    void findByNameInsensitive_shouldReturnAll_whenNameIsNullOrBlank() {
        // Arrange
        List<Category> expected = List.of(new Category(NAME_ACCION));
        when(findCategoryPort.findAll()).thenReturn(expected);

        // Act
        List<Category> resultNull = useCase.findByNameInsensitive(null);
        List<Category> resultEmpty = useCase.findByNameInsensitive(NAME_EMPTY);

        // Assert
        verify(findCategoryPort, times(2)).findAll();
        assertThat(resultNull).isEqualTo(expected);
        assertThat(resultEmpty).isEqualTo(expected);
    }

    @Test
    @DisplayName("debe devolver categorías por nombre insensible a mayúsculas")
    void findByNameInsensitive_shouldReturnMatchingCategories_whenNameProvided() {
        // Arrange
        List<Category> expected = List.of(new Category(NAME_ACCION));
        when(findCategoryPort.findCategoriesByNameInsensitive(NAME_ACCION)).thenReturn(expected);

        // Act
        List<Category> result = useCase.findByNameInsensitive(NAME_ACCION);

        // Assert
        verify(findCategoryPort, times(1)).findCategoriesByNameInsensitive(NAME_ACCION);
        assertThat(result).isEqualTo(expected);
    }
}