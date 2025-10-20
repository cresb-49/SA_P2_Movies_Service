package com.sap.movies_service.movies.application.usecases.createmovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.output.*;
import com.sap.movies_service.movies.application.usecases.createmovie.dtos.CreateMovieDTO;
import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.domain.CategoryView;
import com.sap.movies_service.movies.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMovieCaseTest {

    @Mock private SaveMoviePort saveMoviePort;
    @Mock private SaveImagePort saveImagePort;
    @Mock private FindingCategoriesPort findingCategoriesPort;
    @Mock private FindingClassificationPort findingClassificationPort;
    @Mock private SaveCategoriesMoviePort saveCategoriesMoviePort;
    @Mock private MovieFactory movieFactory;

    @InjectMocks
    private CreateMovieCase useCase;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String BUCKET = "bucket-test";
    private static final String DIR = "movies";
    private static final String REGION = "us-east-1";

    @BeforeEach
    void setUp() throws Exception {
        setField(useCase, "bucketName", BUCKET);
        setField(useCase, "bucketDirectory", DIR);
        setField(useCase, "awsRegion", REGION);
    }

    @Test
    @DisplayName("debe crear la película, guardar categorías, subir imagen y devolver con relaciones")
    void create_shouldCreateSaveUploadAndReturnEnriched() throws IOException {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getContentType()).thenReturn("image/jpeg");
        when(image.getOriginalFilename()).thenReturn("poster.jpg");
        when(image.getBytes()).thenReturn(new byte[]{1,2,3});

        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getTitle()).thenReturn(TITLE);
        when(dto.getDuration()).thenReturn(DURATION);
        when(dto.getSinopsis()).thenReturn(SINOPSIS);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getDirector()).thenReturn(DIRECTOR);
        when(dto.getCasting()).thenReturn(CASTING);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID));
        when(dto.getImage()).thenReturn(image);

        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        CategoryView categoryView = mock(CategoryView.class);
        when(categoryView.id()).thenReturn(CATEGORY_ID);
        when(findingCategoriesPort.findAllById(List.of(CATEGORY_ID))).thenReturn(List.of(categoryView));

        Mockito.lenient().when(saveMoviePort.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));
        Movie enriched = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, "http://x");
        when(movieFactory.movieWithAllRelations(any(Movie.class))).thenReturn(enriched);

        ArgumentCaptor<Movie> movieCaptor = ArgumentCaptor.forClass(Movie.class);
        ArgumentCaptor<List<CategoryMovie>> catMoviesCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        // Act
        Movie result = useCase.create(dto);

        // Assert
        verify(findingClassificationPort, times(1)).existsById(CLASSIFICATION_ID);
        verify(findingCategoriesPort, times(1)).findAllById(List.of(CATEGORY_ID));
        verify(saveMoviePort, times(1)).save(movieCaptor.capture());
        Movie savedMovie = movieCaptor.getValue();
        assertThat(savedMovie.getUrlImage()).startsWith("https://" + BUCKET + ".s3." + REGION + ".amazonaws.com/" + DIR + "/movie-")
                                           .endsWith(".jpg");

        verify(saveCategoriesMoviePort, times(1)).saveCategoriesMovie(catMoviesCaptor.capture());
        List<CategoryMovie> sentCategories = catMoviesCaptor.getValue();
        assertThat(sentCategories).hasSize(1);
        assertThat(sentCategories.get(0).getCategoryId()).isEqualTo(CATEGORY_ID);

        verify(saveImagePort, times(1)).uploadImage(eq(BUCKET), eq(DIR), keyCaptor.capture(), any(byte[].class));
        assertThat(keyCaptor.getValue()).startsWith("movie-").endsWith(".jpg");

        verify(movieFactory, times(1)).movieWithAllRelations(savedMovie);
        assertThat(result).isEqualTo(enriched);
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando la clasificación no existe")
    void create_shouldThrow_whenClassificationNotExists() {
        // Arrange
        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
        verify(saveImagePort, never()).uploadImage(any(), any(), any(), any());
    }

    @Test
    @DisplayName("debe lanzar NotFoundException cuando alguna categoría no existe")
    void create_shouldThrow_whenCategoryMissing() {
        // Arrange
        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID, UUID.randomUUID()));

        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(NotFoundException.class);
        verify(saveMoviePort, never()).save(any());
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando la imagen está vacía")
    void create_shouldThrow_whenImageEmpty() {
        // Arrange
        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID));
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(true);
        when(dto.getImage()).thenReturn(image);
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalArgumentException cuando el tipo de imagen es inválido")
    void create_shouldThrow_whenInvalidContentType() {
        // Arrange
        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID));
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getContentType()).thenReturn("application/pdf");
        when(dto.getImage()).thenReturn(image);
        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("debe lanzar IllegalStateException cuando la imagen no tiene nombre")
    void create_shouldThrow_whenImageFilenameMissing() {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getContentType()).thenReturn("image/png");
        when(image.getOriginalFilename()).thenReturn(null);

        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID));
        when(dto.getImage()).thenReturn(image);

        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(mock(CategoryView.class)));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("debe envolver excepciones de subida de imagen en RuntimeException")
    void create_shouldWrapUploadExceptions() throws Exception {
        // Arrange
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getContentType()).thenReturn("image/jpeg");
        when(image.getOriginalFilename()).thenReturn("poster.jpg");
        when(image.getBytes()).thenThrow(new IOException("io"));

        CreateMovieDTO dto = mock(CreateMovieDTO.class);
        when(dto.getTitle()).thenReturn(TITLE);
        when(dto.getDuration()).thenReturn(DURATION);
        when(dto.getSinopsis()).thenReturn(SINOPSIS);
        when(dto.getClassificationId()).thenReturn(CLASSIFICATION_ID);
        when(dto.getDirector()).thenReturn(DIRECTOR);
        when(dto.getCasting()).thenReturn(CASTING);
        when(dto.getCategoriesId()).thenReturn(List.of(CATEGORY_ID));
        when(dto.getImage()).thenReturn(image);

        when(findingClassificationPort.existsById(CLASSIFICATION_ID)).thenReturn(true);
        CategoryView cv = mock(CategoryView.class);
        when(cv.id()).thenReturn(CATEGORY_ID);
        when(findingCategoriesPort.findAllById(anyList())).thenReturn(List.of(cv));
        when(saveMoviePort.save(any(Movie.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act & Assert
        assertThatThrownBy(() -> useCase.create(dto)).isInstanceOf(RuntimeException.class);
    }

    private static void setField(Object target, String field, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }
}