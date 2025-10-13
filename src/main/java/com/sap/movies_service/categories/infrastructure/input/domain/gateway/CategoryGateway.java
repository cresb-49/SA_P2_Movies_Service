package com.sap.movies_service.categories.infrastructure.input.domain.gateway;

import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.input.domain.port.CategoryGatewayPort;
import com.sap.movies_service.categories.infrastructure.output.jpa.adpater.CategoryJpaAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryGateway implements CategoryGatewayPort {

    private final CategoryJpaAdapter categoryJpaAdapter;

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryJpaAdapter.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryJpaAdapter.existsById(id);
    }

    @Override
    public List<Category> findByIdIn(List<UUID> ids) {
        return categoryJpaAdapter.findInIds(ids);
    }
}
