package com.sap.movies_service.classifications.infrastructure.output.jpa.repository;

import com.sap.movies_service.classifications.infrastructure.output.jpa.entity.ClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClassificationEntityRepository extends JpaRepository<ClassificationEntity, UUID> {
    Optional<ClassificationEntity> findByNameIgnoreCase(String name);
}
