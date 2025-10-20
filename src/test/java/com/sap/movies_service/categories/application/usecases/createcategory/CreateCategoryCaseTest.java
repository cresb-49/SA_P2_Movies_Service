package com.sap.movies_service.categories.application.usecases.createcategory;

import com.sap.common_lib.exception.EntityAlreadyExistsException;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.application.output.SaveCategoryPort;
import com.sap.movies_service.categories.domain.Category;
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
class CreateCategoryCaseTest {

    @Mock
    private FindCategoryPort findCategoryPort;

    @Mock
    private SaveCategoryPort saveCategoryPort;

    @InjectMocks
    private CreateCategoryCase useCase;

    private static final String NAME_ACCION = "Acción";
    private static final String NAME_DRAMA = "Drama";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe crear y guardar la categoría cuando el nombre no existe")
    void create_shouldSaveCategory_whenNameNotExists() {
        // Arrange
        when(findCategoryPort.findByNameInsensitive(NAME_ACCION)).thenReturn(Optional.empty());
        when(saveCategoryPort.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Category result = useCase.create(NAME_ACCION);

        // Assert
        verify(findCategoryPort, times(1)).findByNameInsensitive(NAME_ACCION);
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(saveCategoryPort, times(1)).save(captor.capture());
        Category saved = captor.getValue();
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(NAME_ACCION);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(NAME_ACCION);
    }

    @Test
    @DisplayName("debe lanzar EntityAlreadyExistsException cuando la categoría ya existe")
    void create_shouldThrow_whenNameAlreadyExists() {
        // Arrange
        when(findCategoryPort.findByNameInsensitive(NAME_DRAMA)).thenReturn(Optional.of(new Category(NAME_DRAMA)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(NAME_DRAMA))
                .isInstanceOf(EntityAlreadyExistsException.class);
        verify(saveCategoryPort, never()).save(any());
    }
}