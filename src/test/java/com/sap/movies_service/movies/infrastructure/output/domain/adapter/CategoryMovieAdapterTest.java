

package com.sap.movies_service.movies.infrastructure.output.domain.adapter;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.input.domain.port.CategoryGatewayPort;
import com.sap.movies_service.movies.domain.CategoryView;
import com.sap.movies_service.movies.infrastructure.output.domain.mapper.CategoryViewMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryMovieAdapterTest {

    @Mock
    private CategoryGatewayPort categoryGatewayPort;

    @Mock
    private CategoryViewMapper mapper;

    @InjectMocks
    private CategoryMovieAdapter adapter;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String NAME_ACCION = "Acción";

    @Test
    @DisplayName("existsById debe delegar en el gateway y devolver true")
    void existsById_shouldDelegateAndReturnTrue() {
        // Arrange
        when(categoryGatewayPort.existsById(CATEGORY_ID)).thenReturn(true);

        // Act
        boolean result = adapter.existsById(CATEGORY_ID);

        // Assert
        verify(categoryGatewayPort, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("findAllById debe mapear las categorías a vistas")
    void findAllById_shouldMapCategoriesToViews() {
        // Arrange
        List<UUID> ids = List.of(CATEGORY_ID);
        List<Category> categories = List.of(new Category(NAME_ACCION));
        List<CategoryView> expected = List.of(new CategoryView(CATEGORY_ID, NAME_ACCION));
        when(categoryGatewayPort.findByIdIn(ids)).thenReturn(categories);
        when(mapper.toViewList(categories)).thenReturn(expected);

        // Act
        List<CategoryView> result = adapter.findAllById(ids);

        // Assert
        verify(categoryGatewayPort, times(1)).findByIdIn(ids);
        verify(mapper, times(1)).toViewList(categories);
        assertThat(result).isEqualTo(expected);
    }
}