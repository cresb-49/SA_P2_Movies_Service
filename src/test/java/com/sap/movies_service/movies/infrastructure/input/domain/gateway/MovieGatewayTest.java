

package com.sap.movies_service.movies.infrastructure.input.domain.gateway;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.output.jpa.adapter.MovieJpaAdapter;
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
class MovieGatewayTest {

    @Mock
    private MovieJpaAdapter movieJpaAdapter;

    @InjectMocks
    private MovieGateway movieGateway;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_IMAGE = "http://image.jpg";

    @Test
    @DisplayName("debe retornar true si existen películas con esa clasificación")
    void hasMoviesWithClassificationId_shouldReturnTrue() {
        // Arrange
        when(movieJpaAdapter.hasMoviesWithClassificationId(CLASSIFICATION_ID)).thenReturn(true);

        // Act
        boolean result = movieGateway.hasMoviesWithClassificationId(CLASSIFICATION_ID);

        // Assert
        verify(movieJpaAdapter, times(1)).hasMoviesWithClassificationId(CLASSIFICATION_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe retornar Movie cuando existe por ID")
    void findById_shouldReturnMovie() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        when(movieJpaAdapter.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        // Act
        Optional<Movie> result = movieGateway.findById(MOVIE_ID);

        // Assert
        verify(movieJpaAdapter, times(1)).findById(MOVIE_ID);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(movie);
    }

    @Test
    @DisplayName("debe retornar vacío cuando no existe película por ID")
    void findById_shouldReturnEmpty_whenNotFound() {
        // Arrange
        when(movieJpaAdapter.findById(MOVIE_ID)).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieGateway.findById(MOVIE_ID);

        // Assert
        verify(movieJpaAdapter, times(1)).findById(MOVIE_ID);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("debe retornar true cuando la película existe por ID")
    void existsById_shouldReturnTrue() {
        // Arrange
        when(movieJpaAdapter.existsById(MOVIE_ID)).thenReturn(true);

        // Act
        boolean result = movieGateway.existsById(MOVIE_ID);

        // Assert
        verify(movieJpaAdapter, times(1)).existsById(MOVIE_ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("debe retornar lista de películas por IDs")
    void findByIds_shouldReturnMoviesList() {
        // Arrange
        List<UUID> ids = List.of(MOVIE_ID);
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
        when(movieJpaAdapter.findByIdsIn(ids)).thenReturn(List.of(movie));

        // Act
        List<Movie> result = movieGateway.findByIds(ids);

        // Assert
        verify(movieJpaAdapter, times(1)).findByIdsIn(ids);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(movie);
    }
}