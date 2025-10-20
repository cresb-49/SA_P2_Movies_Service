package com.sap.movies_service.movies.application.factory;

import com.sap.movies_service.movies.application.output.FindingCategoriesMoviePort;
import com.sap.movies_service.movies.application.output.FindingCategoriesPort;
import com.sap.movies_service.movies.application.output.FindingClassificationPort;
import com.sap.movies_service.movies.domain.CategoryView;
import com.sap.movies_service.movies.domain.ClassificationView;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieFactoryTest {

    @Mock
    private FindingClassificationPort findingClassificationPort;

    @Mock
    private FindingCategoriesPort findingCategoriesPort;

    @Mock
    private FindingCategoriesMoviePort findingCategoriesMoviePort;

    @InjectMocks
    private MovieFactory factory;

    private static final UUID CLASSIFICATION_ID = UUID.randomUUID();
    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String TITLE = "Matrix";
    private static final int DURATION = 120;
    private static final String SINOPSIS = "Neo descubre la verdad";
    private static final String DIRECTOR = "Wachowski";
    private static final String CASTING = "Keanu Reeves";
    private static final String URL_IMAGE = "http://image.jpg";

    private Movie baseMovie;

    @BeforeEach
    void setUp() {
        // Arrange
        baseMovie = new Movie(TITLE, DURATION, SINOPSIS, CLASSIFICATION_ID, DIRECTOR, CASTING, URL_IMAGE);
    }

    @Test
    @DisplayName("debe construir una película con todas sus relaciones")
    void movieWithAllRelations_shouldReturnMovieWithRelations() {
        // Arrange
        ClassificationView classificationView = mock(ClassificationView.class);
        CategoryView categoryView = mock(CategoryView.class);
        List<UUID> categoryIds = List.of(CATEGORY_ID);
        List<CategoryView> categories = List.of(categoryView);

        when(findingClassificationPort.findById(baseMovie.getClassificationId())).thenReturn(classificationView);
        when(findingCategoriesMoviePort.findCategoryIdsByMovieId(baseMovie.getId())).thenReturn(categoryIds);
        when(findingCategoriesPort.findAllById(categoryIds)).thenReturn(categories);

        // Act
        Movie result = factory.movieWithAllRelations(baseMovie);

        // Assert
        verify(findingClassificationPort, times(1)).findById(baseMovie.getClassificationId());
        verify(findingCategoriesMoviePort, times(1)).findCategoryIdsByMovieId(baseMovie.getId());
        verify(findingCategoriesPort, times(1)).findAllById(categoryIds);
        assertThat(result).isNotNull();
        assertThat(result).isNotSameAs(baseMovie);
        assertThat(result.getClassification()).isEqualTo(classificationView);
        assertThat(result.getCategories()).isEqualTo(categories);
    }

    @Test
    @DisplayName("debe construir una lista de películas con todas sus relaciones")
    void moviesWithAllRelations_shouldReturnListWithRelations() {
        // Arrange
        ClassificationView classificationView = mock(ClassificationView.class);
        CategoryView categoryView = mock(CategoryView.class);
        List<Movie> movies = List.of(baseMovie);
        List<UUID> categoryIds = List.of(CATEGORY_ID);
        List<CategoryView> categories = List.of(categoryView);

        when(findingClassificationPort.findById(baseMovie.getClassificationId())).thenReturn(classificationView);
        when(findingCategoriesMoviePort.findCategoryIdsByMovieId(baseMovie.getId())).thenReturn(categoryIds);
        when(findingCategoriesPort.findAllById(categoryIds)).thenReturn(categories);

        // Act
        List<Movie> result = factory.moviesWithAllRelations(movies);

        // Assert
        verify(findingClassificationPort, times(1)).findById(baseMovie.getClassificationId());
        verify(findingCategoriesMoviePort, times(1)).findCategoryIdsByMovieId(baseMovie.getId());
        verify(findingCategoriesPort, times(1)).findAllById(categoryIds);
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isNotSameAs(baseMovie);
        assertThat(result.get(0).getClassification()).isEqualTo(classificationView);
        assertThat(result.get(0).getCategories()).isEqualTo(categories);
    }

    @Test
    @DisplayName("debe construir un Page de películas con todas sus relaciones")
    void moviesWithAllRelations_shouldReturnPageWithRelations() {
        // Arrange
        ClassificationView classificationView = mock(ClassificationView.class);
        CategoryView categoryView = mock(CategoryView.class);
        Page<Movie> page = new PageImpl<>(List.of(baseMovie));
        List<UUID> categoryIds = List.of(CATEGORY_ID);
        List<CategoryView> categories = List.of(categoryView);

        when(findingClassificationPort.findById(baseMovie.getClassificationId())).thenReturn(classificationView);
        when(findingCategoriesMoviePort.findCategoryIdsByMovieId(baseMovie.getId())).thenReturn(categoryIds);
        when(findingCategoriesPort.findAllById(categoryIds)).thenReturn(categories);

        // Act
        Page<Movie> result = factory.moviesWithAllRelations(page);

        // Assert
        verify(findingClassificationPort, times(1)).findById(baseMovie.getClassificationId());
        verify(findingCategoriesMoviePort, times(1)).findCategoryIdsByMovieId(baseMovie.getId());
        verify(findingCategoriesPort, times(1)).findAllById(categoryIds);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isNotSameAs(baseMovie);
        assertThat(result.getContent().get(0).getClassification()).isEqualTo(classificationView);
        assertThat(result.getContent().get(0).getCategories()).isEqualTo(categories);
    }
}