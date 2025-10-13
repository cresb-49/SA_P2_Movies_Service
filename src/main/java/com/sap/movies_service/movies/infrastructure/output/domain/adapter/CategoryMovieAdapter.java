package com.sap.movies_service.movies.infrastructure.output.domain.adapter;

import com.sap.movies_service.categories.infrastructure.input.domain.port.CategoryGatewayPort;
import com.sap.movies_service.movies.application.output.FindingCategoriesPort;
import com.sap.movies_service.movies.domain.CategoryView;
import com.sap.movies_service.movies.infrastructure.output.domain.mapper.CategoryViewMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CategoryMovieAdapter implements FindingCategoriesPort {

    private final CategoryGatewayPort categoryGatewayPort;

    private final CategoryViewMapper mapper;

    @Override
    public boolean existsById(UUID id) {
        return categoryGatewayPort.existsById(id);
    }

    @Override
    public List<CategoryView> findAllById(List<UUID> ids) {
        var categories = categoryGatewayPort.findByIdIn(ids);
        return mapper.toViewList(categories);
    }
}
