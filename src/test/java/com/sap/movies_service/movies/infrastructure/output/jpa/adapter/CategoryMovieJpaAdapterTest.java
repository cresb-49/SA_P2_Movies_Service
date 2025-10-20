package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.CategoryMovieEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.mapper.CategoryMovieMapper;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.CategoryMovieEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryMovieJpaAdapterTest {

    @Mock
    private CategoryMovieEntityRepository repository;

    @Mock
    private CategoryMovieMapper mapper;

    @InjectMocks
    private CategoryMovieJpaAdapter adapter;

    private static final UUID MOVIE_ID = UUID.randomUUID();
    private static final UUID CAT_ID_1 = UUID.randomUUID();
    private static final UUID CAT_ID_2 = UUID.randomUUID();

    @Test
    @DisplayName("findCategoryIdsByMovieId debe delegar en el repositorio y devolver lista de IDs")
    void findCategoryIdsByMovieId_shouldReturnList() {
        // Arrange
        List<UUID> expected = List.of(CAT_ID_1, CAT_ID_2);
        when(repository.findCategoryIdsByMovieId(MOVIE_ID)).thenReturn(expected);

        // Act
        List<UUID> result = adapter.findCategoryIdsByMovieId(MOVIE_ID);

        // Assert
        verify(repository, times(1)).findCategoryIdsByMovieId(MOVIE_ID);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("deleteByMovieIdAndCategoryIdIn debe delegar en el repositorio y devolver conteo")
    void deleteByMovieIdAndCategoryIdIn_shouldReturnCount() {
        // Arrange
        List<UUID> ids = List.of(CAT_ID_1, CAT_ID_2);
        when(repository.deleteByMovieIdAndCategoryIdIn(MOVIE_ID, ids)).thenReturn(2);

        // Act
        int result = adapter.deleteByMovieIdAndCategoryIdIn(MOVIE_ID, ids);

        // Assert
        verify(repository, times(1)).deleteByMovieIdAndCategoryIdIn(MOVIE_ID, ids);
        assertThat(result).isEqualTo(2);
    }

    @Test
    @DisplayName("deleteByMovieId debe delegar en el repositorio y devolver conteo")
    void deleteByMovieId_shouldReturnCount() {
        // Arrange
        when(repository.deleteByMovieId(MOVIE_ID)).thenReturn(1);

        // Act
        int result = adapter.deleteByMovieId(MOVIE_ID);

        // Assert
        verify(repository, times(1)).deleteByMovieId(MOVIE_ID);
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("saveCategoriesMovie debe mapear entidades y llamar a saveAll")
    void saveCategoriesMovie_shouldMapAndSaveAll() {
        // Arrange
        CategoryMovie cm1 = new CategoryMovie(CAT_ID_1, MOVIE_ID);
        CategoryMovie cm2 = new CategoryMovie(CAT_ID_2, MOVIE_ID);
        List<CategoryMovie> list = List.of(cm1, cm2);
        CategoryMovieEntity entity1 = mock(CategoryMovieEntity.class);
        CategoryMovieEntity entity2 = mock(CategoryMovieEntity.class);
        when(mapper.toEntity(cm1)).thenReturn(entity1);
        when(mapper.toEntity(cm2)).thenReturn(entity2);

        // Act
        adapter.saveCategoriesMovie(list);

        // Assert
        verify(mapper, times(1)).toEntity(cm1);
        verify(mapper, times(1)).toEntity(cm2);
        verify(repository, times(1)).saveAll(List.of(entity1, entity2));
    }
}