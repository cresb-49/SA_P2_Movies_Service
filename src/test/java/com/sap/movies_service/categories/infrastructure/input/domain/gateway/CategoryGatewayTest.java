

package com.sap.movies_service.categories.infrastructure.input.domain.gateway;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.output.jpa.adpater.CategoryJpaAdapter;
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
class CategoryGatewayTest {

    @Mock
    private CategoryJpaAdapter categoryJpaAdapter;

    @InjectMocks
    private CategoryGateway gateway;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String NAME_ACCION = "Acción";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe devolver la categoría cuando se encuentra por ID")
    void findById_shouldReturnCategory_whenExists() {
        // Arrange
        Category category = new Category(NAME_ACCION);
        when(categoryJpaAdapter.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        // Act
        Optional<Category> result = gateway.findById(CATEGORY_ID);

        // Assert
        verify(categoryJpaAdapter, times(1)).findById(CATEGORY_ID);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(category);
    }

    @Test
    @DisplayName("debe devolver Optional vacío cuando la categoría no existe")
    void findById_shouldReturnEmpty_whenNotExists() {
        // Arrange
        when(categoryJpaAdapter.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        // Act
        Optional<Category> result = gateway.findById(CATEGORY_ID);

        // Assert
        verify(categoryJpaAdapter, times(1)).findById(CATEGORY_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe devolver true cuando la categoría existe por ID")
    void existsById_shouldReturnTrue_whenExists() {
        // Arrange
        when(categoryJpaAdapter.existsById(CATEGORY_ID)).thenReturn(true);

        // Act
        boolean result = gateway.existsById(CATEGORY_ID);

        // Assert
        verify(categoryJpaAdapter, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe devolver false cuando la categoría no existe por ID")
    void existsById_shouldReturnFalse_whenNotExists() {
        // Arrange
        when(categoryJpaAdapter.existsById(CATEGORY_ID)).thenReturn(false);

        // Act
        boolean result = gateway.existsById(CATEGORY_ID);

        // Assert
        verify(categoryJpaAdapter, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe devolver lista de categorías al buscar por IDs")
    void findByIdIn_shouldReturnCategories_whenIdsProvided() {
        // Arrange
        List<UUID> ids = List.of(CATEGORY_ID);
        List<Category> expected = List.of(new Category(NAME_ACCION));
        when(categoryJpaAdapter.findInIds(ids)).thenReturn(expected);

        // Act
        List<Category> result = gateway.findByIdIn(ids);

        // Assert
        verify(categoryJpaAdapter, times(1)).findInIds(ids);
        assertThat(result).isEqualTo(expected);
    }
}