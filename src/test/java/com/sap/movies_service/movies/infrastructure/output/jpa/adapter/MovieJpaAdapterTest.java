

package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.mapper.MovieMapper;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.MovieEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieJpaAdapterTest {

    @Mock
    private MovieEntityRepository repository;

    @Mock
    private MovieMapper mapper;

    @InjectMocks
    private MovieJpaAdapter adapter;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_IMAGE = "http://image.jpg";

    @Test
    @DisplayName("existsById debe delegar y devolver true")
    void existsById_shouldReturnTrue() {
        // Arrange
        when(repository.existsById(MOVIE_ID)).thenReturn(true);
        // Act
        boolean result = adapter.existsById(MOVIE_ID);
        // Assert
        verify(repository, times(1)).existsById(MOVIE_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("findById debe mapear a dominio cuando existe")
    void findById_shouldMap_whenPresent() {
        // Arrange
        MovieEntity entity = mock(MovieEntity.class);
        Movie domain = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        when(repository.findById(MOVIE_ID)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);
        // Act
        Optional<Movie> result = adapter.findById(MOVIE_ID);
        // Assert
        verify(repository, times(1)).findById(MOVIE_ID);
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);
    }

    @Test
    @DisplayName("findById debe devolver Optional vacío cuando no existe")
    void findById_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(repository.findById(MOVIE_ID)).thenReturn(Optional.empty());
        // Act
        Optional<Movie> result = adapter.findById(MOVIE_ID);
        // Assert
        verify(repository, times(1)).findById(MOVIE_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAll debe paginar y mapear a dominio")
    void findAll_shouldReturnPagedMapped() {
        // Arrange
        MovieEntity entity = mock(MovieEntity.class);
        Page<MovieEntity> pageEntities = new PageImpl<>(List.of(entity));
        when(repository.findAll(PageRequest.of(0, 20))).thenReturn(pageEntities);
        when(mapper.toDomain(entity)).thenReturn(new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE));
        // Act
        Page<Movie> result = adapter.findAll(0);
        // Assert
        verify(repository, times(1)).findAll(PageRequest.of(0, 20));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("findByFilter debe usar Specification y mapear resultados")
    void findByFilter_shouldUseSpecAndMap() {
        // Arrange
        MovieFilter filter = mock(MovieFilter.class);
        MovieEntity entity = mock(MovieEntity.class);
        Page<MovieEntity> pageEntities = new PageImpl<>(List.of(entity));
        when(repository.findAll(any(Specification.class), eq(PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "createdAt"))))).
                thenReturn(pageEntities);
        when(mapper.toDomain(entity)).thenReturn(new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE));
        // Act
        Page<Movie> result = adapter.findByFilter(filter, 1);
        // Assert
        verify(repository, times(1)).findAll(any(Specification.class), eq(PageRequest.of(1, 20, Sort.by(Sort.Direction.DESC, "createdAt"))));
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("findByIdsIn debe mapear la lista de entidades a dominio")
    void findByIdsIn_shouldMapList() {
        // Arrange
        MovieEntity entity = mock(MovieEntity.class);
        when(repository.findByIdIn(List.of(MOVIE_ID))).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE));
        // Act
        List<Movie> result = adapter.findByIdsIn(List.of(MOVIE_ID));
        // Assert
        verify(repository, times(1)).findByIdIn(List.of(MOVIE_ID));
        verify(mapper, times(1)).toDomain(entity);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("hasMoviesWithClassificationId debe delegar en el repositorio")
    void hasMoviesWithClassificationId_shouldDelegate() {
        // Arrange
        when(repository.existsByClassificationId(CLASSIFICATION_ID)).thenReturn(true);
        // Act
        boolean result = adapter.hasMoviesWithClassificationId(CLASSIFICATION_ID);
        // Assert
        verify(repository, times(1)).existsByClassificationId(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("save debe mapear a entidad, guardar y mapear a dominio")
    void save_shouldMapAndReturnDomain() {
        // Arrange
        Movie domain = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        MovieEntity entity = mock(MovieEntity.class);
        MovieEntity savedEntity = mock(MovieEntity.class);
        Movie mappedDomain = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        when(mapper.toEntity(domain)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(mappedDomain);
        // Act
        Movie result = adapter.save(domain);
        // Assert
        verify(mapper, times(1)).toEntity(domain);
        verify(repository, times(1)).save(entity);
        verify(mapper, times(1)).toDomain(savedEntity);
        assertThat(result).isEqualTo(mappedDomain);
    }

    @Test
    @DisplayName("deleteMovieById debe delegar en el repositorio")
    void deleteMovieById_shouldDelegate() {
        // Arrange
        // Act
        adapter.deleteMovieById(MOVIE_ID);
        // Assert
        verify(repository, times(1)).deleteById(MOVIE_ID);
    }
}