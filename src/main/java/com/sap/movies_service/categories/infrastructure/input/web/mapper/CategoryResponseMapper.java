package com.sap.movies_service.categories.infrastructure.input.web.mapper;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.input.web.dtos.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryResponseMapper {
    public CategoryResponseDTO toDTO(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public List<CategoryResponseDTO> toDTOList(List<Category> categories) {
        return categories.stream().map(this::toDTO).toList();
    }
}
