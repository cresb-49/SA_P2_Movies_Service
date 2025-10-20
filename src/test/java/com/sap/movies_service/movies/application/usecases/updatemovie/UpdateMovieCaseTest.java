

package com.sap.movies_service.movies.application.usecases.updatemovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.output.*;
import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.domain.CategoryView;
import com.sap.movies_service.movies.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMovieCaseTest {

    @Mock private FindingMoviePort findingMoviePort;
    @Mock private SaveImagePort saveImagePort;
    @Mock private DeletingImagePort deletingImagePort;
    @Mock private SaveMoviePort saveMoviePort;
    @Mock private FindingCategoriesPort findingCategoriesPort;
    @Mock private FindingClassificationPort findingClassificationPort;
    @Mock private ModifyCategoriesMoviePort modifyCategoriesMoviePort;
    @Mock private FindingCategoriesMoviePort findingCategoriesMoviePort;
    @Mock private SaveCategoriesMoviePort saveCategoriesMoviePort;
    @Mock private MovieFactory movieFactory;

    @InjectMocks private UpdateMovieCase useCase;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final UUID CAT_1 = UUID.randomUUID();
    private static final UUID CAT_2 = UUID.randomUUID();
    private static final UUID CAT_3 = UUID.randomUUID();

    private static final String TITLE = "Matrix Reloaded";
    private static final int DURATION = 130;
    private static final String SINOPSIS = "La segunda parte";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_OLD = "https://bucket-test.s3.us-east-1.amazonaws.com/movies/old-123.jpg";
    private static final String BUCKET = "bucket-test";
    private static final String DIR = "movies";
    private static final String REGION = "us-east-1";

    private Movie existing;

    @BeforeEach
    void setUp() throws Exception {
        setField(useCase, "bucketName", BUCKET);
        setField(useCase, "bucketDirectory", DIR);
        setField(useCase, "awsRegion", REGION);
        existing = new Movie("Matrix", 120, "Neo", CLASSIFICATION_ID, "Dir", "Cast", URL_OLD);
        // override the random id with fixed MOVIE_ID via reflection (only for stable verifications)
        setField(existing, "id", MOVIE_ID);
    }

    @Test
    @DisplayName("debe actualizar datos y categorías sin cambiar imagen")
    void update_shouldUpdateDataAndCategories_withoutImage() {
        // Arrange
        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(dto.getTitle()).thenReturn(TITLE);
        when(dto.getDuration()).thenReturn(DURATION);
        when(dto.getSinopsis()).thenReturn(SINOPSIS);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getDirector()).thenReturn(DIRECTOR);
        when(dto.getCasting()).thenReturn(CASTING);
        when(dto.getImage()).thenReturn(null);
        when(dto.getCategoryIds()).thenReturn(List.of(CAT_2, CAT_3));

        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(existing));
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(List.of(CAT_2, CAT_3))).thenReturn(List.of(mock(CategoryView.class), mock(CategoryView.class)));
        when(findingCategoriesMoviePort.findCategoryIdsByMovieId(MOVIE_ID)).thenReturn(List.of(CAT_1, CAT_2));
        when(saveMoviePort.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movieFactory.movieWithAllRelations(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<List<CategoryMovie>> catMoviesCaptor = ArgumentCaptor.forClass(List.class);

        // Act
        Movie result = useCase.update(dto);

        // Assert
        verify(saveImagePort, never()).uploadImage(any(), any(), any(), any());
        verify(deletingImagePort, never()).deleteImage(any(), any(), any());
        verify(modifyCategoriesMoviePort, times(1)).deleteByMovieIdAndCategoryIdIn(eq(MOVIE_ID), eq(List.of(CAT_1)));
        verify(saveCategoriesMoviePort, times(1)).saveCategoriesMovie(catMoviesCaptor.capture());
        List<CategoryMovie> toAdd = catMoviesCaptor.getValue();
        assertThat(toAdd).hasSize(1);
        assertThat(toAdd.get(0).getCategoryId()).isEqualTo(CAT_3);
        assertThat(result.getTitle()).isEqualTo(TITLE);
        assertThat(result.getDuration()).isEqualTo(DURATION);
        assertThat(result.getSinopsis()).isEqualTo(SINOPSIS);
    }

    @Test
    @DisplayName("debe actualizar datos y reemplazar imagen en S3")
    void update_shouldUpdateDataAndReplaceImage() throws Exception {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("new.png");
        when(image.getBytes()).thenReturn(new byte[]{1,2,3});

        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(dto.getTitle()).thenReturn(TITLE);
        when(dto.getDuration()).thenReturn(DURATION);
        when(dto.getSinopsis()).thenReturn(SINOPSIS);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getDirector()).thenReturn(DIRECTOR);
        when(dto.getCasting()).thenReturn(CASTING);
        when(dto.getImage()).thenReturn(image);
        when(dto.getCategoryIds()).thenReturn(List.of(CAT_1, CAT_2));

        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(existing));
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(List.of(CAT_1, CAT_2))).thenReturn(List.of(mock(CategoryView.class), mock(CategoryView.class)));
        when(findingCategoriesMoviePort.findCategoryIdsByMovieId(MOVIE_ID)).thenReturn(List.of(CAT_1, CAT_2));
        when(saveMoviePort.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));
        when(movieFactory.movieWithAllRelations(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        Movie result = useCase.update(dto);

        // Assert
        verify(saveMoviePort, times(1)).save(movieCaptor.capture());
        Movie saved = movieCaptor.getValue();
        assertThat(saved.getUrlImage()).startsWith("https://" + BUCKET + ".s3." + REGION + ".amazonaws.com/" + DIR + "/movie-")
                                      .endsWith(".png");
        verify(saveImagePort, times(1)).uploadImage(eq(BUCKET), eq(DIR), keyCaptor.capture(), any(byte[].class));
        assertThat(keyCaptor.getValue()).startsWith("movie-").endsWith(".png");
        verify(deletingImagePort, times(1)).deleteImage(eq(BUCKET), eq(DIR), eq("old-123.jpg"));
        assertThat(result.getTitle()).isEqualTo(TITLE);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la película no existe")
    void update_shouldThrow_whenMovieNotFound() {
        // Arrange
        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto)).isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la clasificación no existe")
    void update_shouldThrow_whenClassificationNotExists() {
        // Arrange
        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(existing));
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto)).isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando faltan categorías")
    void update_shouldThrow_whenSomeCategoriesMissing() {
        // Arrange
        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoryIds()).thenReturn(List.of(CAT_1, CAT_2));

        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(existing));
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto)).isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar IllegalStateException cuando el nombre de archivo de imagen es nulo en actualización con imagen")
    void update_shouldThrow_whenImageFilenameMissing() {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn(null);

        UpdateMovieDTO dto = mock(UpdateMovieDTO.class);
        when(dto.getId()).thenReturn(MOVIE_ID);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoryIds()).thenReturn(List.of(CAT_1));
        when(dto.getImage()).thenReturn(image);

        when(findingMoviePort.findById(MOVIE_ID)).thenReturn(Optional.of(existing));
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.update(dto)).isInstanceOf(IllegalStateException.class);
        verify(saveMoviePort, never()).save(any());
        verify(saveImagePort, never()).uploadImage(any(), any(), any(), any());
    }

    private static void setField(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}