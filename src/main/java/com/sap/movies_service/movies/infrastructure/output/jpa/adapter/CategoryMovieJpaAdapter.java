package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.output.FindingCategoriesMoviePort;
import com.sap.movies_service.movies.application.output.ModifyCategoriesMoviePort;
import com.sap.movies_service.movies.application.output.SaveCategoriesMoviePort;
import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.infrastructure.output.jpa.mapper.CategoryMovieMapper;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.CategoryMovieEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CategoryMovieJpaAdapter implements ModifyCategoriesMoviePort, FindingCategoriesMoviePort, SaveCategoriesMoviePort {

    private final CategoryMovieEntityRepository categoryMovieEntityRepository;
    private final CategoryMovieMapper categoryMovieMapper;

    public List<UUID> findCategoryIdsByMovieId(UUID movieId) {
        return categoryMovieEntityRepository.findCategoryIdsByMovieId(movieId);
    }

    @Override
    public int deleteByMovieIdAndCategoryIdIn(UUID movieId, List<UUID> categoryIds) {
        return categoryMovieEntityRepository.deleteByMovieIdAndCategoryIdIn(movieId, categoryIds);
    }

    @Override
    public int deleteByMovieId(UUID movieId) {
        return categoryMovieEntityRepository.deleteByMovieId(movieId);
    }

    @Override
    public void saveCategoriesMovie(List<CategoryMovie> categoryMovies) {
        var entities = categoryMovies.stream()
                .map(categoryMovieMapper::toEntity)
                .toList();
        categoryMovieEntityRepository.saveAll(entities);
    }
}
