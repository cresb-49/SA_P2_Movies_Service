

package com.sap.movies_service.movies.application.usecases.findmovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.usecases.findmovie.dtos.MovieFilter;
import com.sap.movies_service.movies.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMovieCaseTest {

    @Mock
    private FindingMoviePort findingMoviePort;

    @Mock
    private MovieFactory movieFactory;

    @InjectMocks
    private FindMovieCase useCase;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final String MOVIE_ID_STRING = MOVIE_ID.toString();
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_IMAGE = "http://image.jpg";

    private Movie movie;

    @BeforeEach
    void setUp() {
        // Arrange
        movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
    }

    @Test
    @DisplayName("debe devolver la película con todas sus relaciones por ID")
    void findById_shouldReturnMovieWithRelations_whenExists() {
        // Arrange
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(movieFactory.movieWithAllRelations(movie)).thenReturn(movie);

        // Act
        Movie result = useCase.findById(MOVIE_ID_STRING);

        // Assert
        verify(findingMoviePort, times(1)).findById(MOVIE_ID);
        verify(movieFactory, times(1)).movieWithAllRelations(movie);
        assertThat(result).isEqualTo(movie);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la película no existe")
    void findById_shouldThrow_whenNotExists() {
        // Arrange
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.findById(MOVIE_ID_STRING))
                .isInstanceOf(NotFoundException.class);
        verify(movieFactory, never()).movieWithAllRelations(any());
    }

    @Test
    @DisplayName("debe devolver página de películas con relaciones")
    void findAll_shouldReturnPageWithRelations() {
        // Arrange
        Page<Movie> movies = new PageImpl<>(List.of(movie));
        when(findingMoviePort.findAll(0)).thenReturn(movies);
        when(movieFactory.moviesWithAllRelations(movies)).thenReturn(movies);

        // Act
        Page<Movie> result = useCase.findAll(0);

        // Assert
        verify(findingMoviePort, times(1)).findAll(0);
        verify(movieFactory, times(1)).moviesWithAllRelations(movies);
        assertThat(result).isEqualTo(movies);
    }

    @Test
    @DisplayName("debe devolver página filtrada de películas con relaciones")
    void findByFilter_shouldReturnPageWithRelations() {
        // Arrange
        MovieFilter filter = mock(MovieFilter.class);
        Page<Movie> movies = new PageImpl<>(List.of(movie));
        when(findingMoviePort.findByFilter(filter, 0)).thenReturn(movies);
        when(movieFactory.moviesWithAllRelations(movies)).thenReturn(movies);

        // Act
        Page<Movie> result = useCase.findByFilter(filter, 0);

        // Assert
        verify(findingMoviePort, times(1)).findByFilter(filter, 0);
        verify(movieFactory, times(1)).moviesWithAllRelations(movies);
        assertThat(result).isEqualTo(movies);
    }

    @Test
    @DisplayName("debe devolver lista de películas con relaciones por IDs")
    void findByIdsIn_shouldReturnListWithRelations() {
        // Arrange
        List<UUID> ids = List.of(MOVIE_ID);
        List<Movie> movies = List.of(movie);
        when(findingMoviePort.findByIdsIn(ids)).thenReturn(movies);
        when(movieFactory.moviesWithAllRelations(movies)).thenReturn(movies);

        // Act
        List<Movie> result = useCase.findByIdsIn(ids);

        // Assert
        verify(findingMoviePort, times(1)).findByIdsIn(ids);
        verify(movieFactory, times(1)).moviesWithAllRelations(movies);
        assertThat(result).isEqualTo(movies);
    }
}