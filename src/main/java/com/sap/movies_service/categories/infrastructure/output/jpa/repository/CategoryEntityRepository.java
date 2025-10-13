package com.sap.movies_service.categories.infrastructure.output.jpa.repository;

import com.sap.movies_service.categories.infrastructure.output.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, UUID> {
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
    Optional<CategoryEntity> findByNameIgnoreCaseAndIdNot(String name, UUID excludeId);
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);
}
