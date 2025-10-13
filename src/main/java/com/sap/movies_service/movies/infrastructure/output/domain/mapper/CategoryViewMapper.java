package com.sap.movies_service.movies.infrastructure.output.domain.mapper;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.movies.domain.CategoryView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryViewMapper {

    public CategoryView toView(Category category) {
        return new CategoryView(
                category.getId(),
                category.getName()
        );
    }

    public List<CategoryView> toViewList(List<Category> categories) {
        return categories.stream().map(this::toView).toList();
    }

}
