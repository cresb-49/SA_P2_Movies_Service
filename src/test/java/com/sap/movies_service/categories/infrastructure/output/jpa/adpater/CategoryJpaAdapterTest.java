

package com.sap.movies_service.categories.infrastructure.output.jpa.adpater;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.output.jpa.entity.CategoryEntity;
import com.sap.movies_service.categories.infrastructure.output.jpa.mapper.CategoryMapper;
import com.sap.movies_service.categories.infrastructure.output.jpa.repository.CategoryEntityRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryJpaAdapterTest {

    @Mock
    private CategoryEntityRepository repository;

    @Mock
    private CategoryMapper mapper;

    @InjectMocks
    private CategoryJpaAdapter adapter;

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String NAME_ACCION = "Acción";
    private static final String NAME_DRAMA = "Drama";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe eliminar por ID usando el repositorio")
    void deleteById_shouldCallRepository() {
        // Arrange
        // Act
        adapter.deleteById(CATEGORY_ID);
        // Assert
        verify(repository, times(1)).deleteById(CATEGORY_ID);
    }

    @Test
    @DisplayName("debe indicar existencia por ID - true")
    void existsById_shouldReturnTrue() {
        // Arrange
        when(repository.existsById(CATEGORY_ID)).thenReturn(true);
        // Act
        boolean result = adapter.existsById(CATEGORY_ID);
        // Assert
        verify(repository, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe indicar existencia por ID - false")
    void existsById_shouldReturnFalse() {
        // Arrange
        when(repository.existsById(CATEGORY_ID)).thenReturn(false);
        // Act
        boolean result = adapter.existsById(CATEGORY_ID);
        // Assert
        verify(repository, times(1)).existsById(CATEGORY_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe encontrar por ID y mapear a dominio")
    void findById_shouldMapToDomain_whenPresent() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_ACCION);
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Category> result = adapter.findById(CATEGORY_ID);
        // Assert
        verify(repository, times(1)).findById(CATEGORY_ID);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("debe devolver Optional vacío cuando no encuentra por ID")
    void findById_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.empty());
        // Act
        Optional<Category> result = adapter.findById(CATEGORY_ID);
        // Assert
        verify(repository, times(1)).findById(CATEGORY_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe encontrar por nombre insensible y mapear a dominio")
    void findByNameInsensitive_shouldMap_whenPresent() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_ACCION);
        when(repository.findByNameIgnoreCase(NAME_ACCION)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Category> result = adapter.findByNameInsensitive(NAME_ACCION);
        // Assert
        verify(repository, times(1)).findByNameIgnoreCase(NAME_ACCION);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("debe encontrar por nombre insensible excluyendo ID y mapear a dominio")
    void findByNameInsensitive_excludingId_shouldMap_whenPresent() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_DRAMA);
        when(repository.findByNameIgnoreCaseAndIdNot(NAME_DRAMA, CATEGORY_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Category> result = adapter.findByNameInsensitive(NAME_DRAMA, CATEGORY_ID);
        // Assert
        verify(repository, times(1)).findByNameIgnoreCaseAndIdNot(NAME_DRAMA, CATEGORY_ID);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("debe devolver lista mapeada al buscar por IDs")
    void findInIds_shouldReturnMappedList() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_ACCION);
        List<UUID> ids = List.of(CATEGORY_ID);
        when(repository.findAllById(ids)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        List<Category> result = adapter.findInIds(ids);
        // Assert
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).containsExactly(domain);
    }

    @Test
    @DisplayName("debe devolver todas las categorías mapeadas")
    void findAll_shouldReturnMappedList() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_ACCION);
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        List<Category> result = adapter.findAll();
        // Assert
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).containsExactly(domain);
    }

    @Test
    @DisplayName("debe buscar por nombre insensible y devolver lista mapeada")
    void findCategoriesByNameInsensitive_shouldReturnMappedList() {
        // Arrange
        CategoryEntity entity = mock(CategoryEntity.class);
        Category domain = new Category(NAME_DRAMA);
        when(repository.findByNameContainingIgnoreCase(NAME_DRAMA)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        List<Category> result = adapter.findCategoriesByNameInsensitive(NAME_DRAMA);
        // Assert
        verify(repository, times(1)).findByNameContainingIgnoreCase(NAME_DRAMA);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).containsExactly(domain);
    }

    @Test
    @DisplayName("debe guardar mapeando entidad y devolviendo dominio")
    void save_shouldMapEntityAndReturnDomain() {
        // Arrange
        Category domain = new Category(NAME_ACCION);
        CategoryEntity entity = mock(CategoryEntity.class);
        CategoryEntity savedEntity = mock(CategoryEntity.class);
        Category mappedDomain = new Category(NAME_ACCION);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(mappedDomain);
        // Act
        Category result = adapter.save(domain);
        // Assert
        verify(mapper, times(1)).toEntity(domain);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDomain(savedEntity);
        assertThat(result).isEqualTo(mappedDomain);
    }
}