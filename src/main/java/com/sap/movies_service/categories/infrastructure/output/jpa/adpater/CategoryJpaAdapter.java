package com.sap.movies_service.categories.infrastructure.output.jpa.adpater;

import com.sap.movies_service.categories.application.output.DeleteCategoryPort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.application.output.SaveCategoryPort;
import com.sap.movies_service.categories.domain.Category;
import com.sap.movies_service.categories.infrastructure.output.jpa.mapper.CategoryMapper;
import com.sap.movies_service.categories.infrastructure.output.jpa.repository.CategoryEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CategoryJpaAdapter implements DeleteCategoryPort, SaveCategoryPort, FindCategoryPort {

    private final CategoryEntityRepository categoryEntityRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public void deleteById(UUID id) {
        categoryEntityRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return categoryEntityRepository.existsById(id);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return categoryEntityRepository.findById(id).map(categoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByNameInsensitive(String name) {
        return categoryEntityRepository.findByNameIgnoreCase(name).map(categoryMapper::toDomain);
    }

    @Override
    public Optional<Category> findByNameInsensitive(String name, UUID excludeId) {
        return categoryEntityRepository.findByNameIgnoreCaseAndIdNot(name, excludeId)
                .map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findInIds(List<UUID> ids) {
        return categoryEntityRepository.findAllById(ids)
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findAll() {
        return categoryEntityRepository.findAll()
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findCategoriesByNameInsensitive(String name) {
        return categoryEntityRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public Category save(Category category) {
        var entity = categoryMapper.toEntity(category);
        var savedEntity = categoryEntityRepository.save(entity);
        return categoryMapper.toDomain(savedEntity);
    }
}
