package com.sap.movies_service.movies.infrastructure.input.web.mappers;

import com.sap.common_lib.dto.response.movie.CategoryViewResponseDTO;
import com.sap.movies_service.movies.domain.CategoryView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryViewResponseMapper {

    public CategoryViewResponseDTO toResponse(CategoryView categoryView) {
        if (categoryView == null) {
            return null;
        }
        return new CategoryViewResponseDTO(
                categoryView.id(),
                categoryView.name()
        );
    }

    public List<CategoryViewResponseDTO> toResponseList(List<CategoryView> categoryViews) {
        if (categoryViews == null) {
            return null;
        }
        return categoryViews.stream().map(this::toResponse).toList();
    }
}
