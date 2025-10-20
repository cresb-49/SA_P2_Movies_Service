

package com.sap.movies_service.movies.application.usecases.deletemovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.output.DeletingImagePort;
import com.sap.movies_service.movies.application.output.DeletingMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMovieCaseTest {

    @Mock private FindingMoviePort findingMoviePort;
    @Mock private DeletingMoviePort deletingMoviePort;
    @Mock private DeletingImagePort deletingImagePort;

    @InjectMocks
    private DeleteMovieCase useCase;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String BUCKET = "bucket-test";
    private static final String DIR = "movies";

    @BeforeEach
    void setUp() throws Exception {
        setField(useCase, "bucketName", BUCKET);
        setField(useCase, "bucketDirectory", DIR);
        setField(useCase, "awsRegion", "us-east-1");
    }

    @Test
    @DisplayName("debe eliminar la película y su imagen cuando existe")
    void delete_shouldRemoveMovieAndImage_whenExists() {
        // Arrange
        String key = "movie-123.jpg";
        String urlImage = "https://" + BUCKET + ".s3.us-east-1.amazonaws.com/" + DIR + "/" + key;
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, urlImage);
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        // Act
        useCase.delete(MOVIE_ID);

        // Assert
        verify(findingMoviePort, times(1)).findById(MOVIE_ID);
        verify(deletingMoviePort, times(1)).deleteMovieById(MOVIE_ID);
        verify(deletingImagePort, times(1)).deleteImage(BUCKET, DIR, key);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la película no existe")
    void delete_shouldThrow_whenMovieNotFound() {
        // Arrange
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.delete(MOVIE_ID))
                .isInstanceOf(NotFoundException.class);
        verify(deletingMoviePort, never()).deleteMovieById(any());
        verify(deletingImagePort, never()).deleteImage(any(), any(), any());
    }

    @Test
    @DisplayName("debe lanzar RuntimeException si la imagen no existe y no intentar borrar en S3")
    void delete_shouldWrap_whenImageUrlMissing() {
        // Arrange
        Movie movie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, null);
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

        // Act & Assert
        assertThatThrownBy(() -> useCase.delete(MOVIE_ID))
                .isInstanceOf(RuntimeException.class);
        verify(deletingMoviePort, times(1)).deleteMovieById(MOVIE_ID);
        verify(deletingImagePort, never()).deleteImage(any(), any(), any());
    }

    private static void setField(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}