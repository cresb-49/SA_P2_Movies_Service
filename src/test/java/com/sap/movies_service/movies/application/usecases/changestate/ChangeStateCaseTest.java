

package com.sap.movies_service.movies.application.usecases.changestate;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeStateCaseTest {

    @Mock
    private FindingMoviePort findingMoviePort;

    @Mock
    private SaveMoviePort saveMoviePort;

    @Mock
    private MovieFactory movieFactory;

    @InjectMocks
    private ChangeStateCase useCase;

    private static final UUID MOVIE_ID = UUID.randomUUID();
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
    @DisplayName("debe cambiar el estado de la película, guardar y devolverla con relaciones")
    void changeState_shouldToggleSaveAndReturnWithRelations() {
        // Arrange
        Movie enriched = new Movie(movie);
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
        when(saveMoviePort.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movieFactory.movieWithAllRelations(any(Movie.class))).thenReturn(enriched);

        // Act
        Movie result = useCase.changeState(MOVIE_ID);

        // Assert
        verify(findingMoviePort, times(1)).findById(MOVIE_ID);
        ArgumentCaptor<Movie> captor = ArgumentCaptor.forClass(Movie.class);
        verify(saveMoviePort, times(1)).save(captor.capture());
        Movie saved = captor.getValue();
        assertThat(saved.isActive()).isFalse();
        verify(movieFactory, times(1)).movieWithAllRelations(saved);
        assertThat(result).isEqualTo(enriched);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la película no existe")
    void changeState_shouldThrow_whenMovieNotFound() {
        // Arrange
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.changeState(MOVIE_ID))
                .isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
        verify(movieFactory, never()).movieWithAllRelations(any());
    }
}