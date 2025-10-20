package com.sap.movies_service.categories.application.usecases.findcategory;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.input.FindCategoryCasePort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.domain.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FindCategoryCase implements FindCategoryCasePort {

    private final FindCategoryPort findCategoryPort;

    @Override
    public boolean existsById(UUID id) {
        return findCategoryPort.existsById(id);
    }

    @Override
    public Category findById(UUID id) {
        return findCategoryPort.findById(id).orElseThrow(
                () -> new NotFoundException("La categoría con id " + id + " no existe")
        );
    }

    @Override
    public List<Category> findAll() {
        return findCategoryPort.findAll();
    }

    @Override
    public List<Category> findAllById(List<UUID> ids) {
        return findCategoryPort.findInIds(ids);
    }

    @Override
    public List<Category> findByNameInsensitive(String name) {
        if (name == null || name.isBlank()) {
            return findAll();
        }
        return findCategoryPort.findCategoriesByNameInsensitive(name);
    }
}
