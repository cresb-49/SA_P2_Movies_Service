

package com.sap.movies_service.classifications.infrastructure.output.jpa.adapter;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.output.jpa.entity.ClassificationEntity;
import com.sap.movies_service.classifications.infrastructure.output.jpa.mapper.ClassificationMapper;
import com.sap.movies_service.classifications.infrastructure.output.jpa.repository.ClassificationEntityRepository;
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
class ClassificationJpaAdapterTest {

    @Mock
    private ClassificationEntityRepository repository;

    @Mock
    private ClassificationMapper mapper;

    @InjectMocks
    private ClassificationJpaAdapter adapter;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String NAME_PG13 = "PG-13";
    private static final String DESC_GENERAL = "Apta para mayores de 13 años";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("debe eliminar por ID usando el repositorio")
    void deleteById_shouldCallRepository() {
        // Arrange
        // Act
        adapter.deleteById(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).deleteById(CLASSIFICATION_ID);
    }

    @Test
    @DisplayName("debe indicar existencia por ID - true")
    void existsById_shouldReturnTrue() {
        // Arrange
        when(repository.existsById(CLASSIFICATION_ID)).thenReturn(true);
        // Act
        boolean result = adapter.existsById(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe indicar existencia por ID - false")
    void existsById_shouldReturnFalse() {
        // Arrange
        when(repository.existsById(CLASSIFICATION_ID)).thenReturn(false);
        // Act
        boolean result = adapter.existsById(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).existsById(CLASSIFICATION_ID);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("debe encontrar por ID y mapear a dominio")
    void findById_shouldMapToDomain_whenPresent() {
        // Arrange
        ClassificationEntity entity = mock(ClassificationEntity.class);
        Classification domain = new Classification(NAME_PG13, DESC_GENERAL);
        when(repository.findById(CLASSIFICATION_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Classification> result = adapter.findById(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).findById(CLASSIFICATION_ID);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("debe devolver Optional vacío cuando no encuentra por ID")
    void findById_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(repository.findById(CLASSIFICATION_ID)).thenReturn(Optional.empty());
        // Act
        Optional<Classification> result = adapter.findById(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).findById(CLASSIFICATION_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe encontrar por nombre insensible y mapear a dominio")
    void findByNameIgnoreCase_shouldMap_whenPresent() {
        // Arrange
        ClassificationEntity entity = mock(ClassificationEntity.class);
        Classification domain = new Classification(NAME_PG13, DESC_GENERAL);
        when(repository.findByNameIgnoreCase(NAME_PG13)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Classification> result = adapter.findByNameIgnoreCase(NAME_PG13);
        // Assert
        verify(repository, times(1)).findByNameIgnoreCase(NAME_PG13);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("debe devolver Optional vacío cuando no encuentra por nombre insensible")
    void findByNameIgnoreCase_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(repository.findByNameIgnoreCase(NAME_PG13)).thenReturn(Optional.empty());
        // Act
        Optional<Classification> result = adapter.findByNameIgnoreCase(NAME_PG13);
        // Assert
        verify(repository, times(1)).findByNameIgnoreCase(NAME_PG13);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe devolver todas las clasificaciones mapeadas")
    void findAll_shouldReturnMappedList() {
        // Arrange
        ClassificationEntity entity = mock(ClassificationEntity.class);
        Classification domain = new Classification(NAME_PG13, DESC_GENERAL);
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        List<Classification> result = adapter.findAll();
        // Assert
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).containsExactly(domain);
    }

    @Test
    @DisplayName("debe buscar por IDs y devolver lista mapeada")
    void findAllById_shouldReturnMappedList() {
        // Arrange
        ClassificationEntity entity = mock(ClassificationEntity.class);
        Classification domain = new Classification(NAME_PG13, DESC_GENERAL);
        List<UUID> ids = List.of(CLASSIFICATION_ID);
        when(repository.findAllById(ids)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        List<Classification> result = adapter.findAllById(ids);
        // Assert
        verify(repository, times(1)).findAllById(ids);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).containsExactly(domain);
    }

    @Test
    @DisplayName("debe guardar mapeando entidad y devolviendo dominio")
    void save_shouldMapEntityAndReturnDomain() {
        // Arrange
        Classification domain = new Classification(NAME_PG13, DESC_GENERAL);
        ClassificationEntity entity = mock(ClassificationEntity.class);
        ClassificationEntity savedEntity = mock(ClassificationEntity.class);
        Classification mappedDomain = new Classification(NAME_PG13, DESC_GENERAL);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(mappedDomain);
        // Act
        Classification result = adapter.save(domain);
        // Assert
        verify(mapper, times(1)).toEntity(domain);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDomain(savedEntity);
        assertThat(result).isEqualTo(mappedDomain);
    }
}