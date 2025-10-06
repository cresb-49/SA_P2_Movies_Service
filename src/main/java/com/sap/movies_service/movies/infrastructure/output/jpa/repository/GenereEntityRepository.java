package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenereEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenereEntityRepository extends JpaRepository<GenereEntity, UUID> {

    Optional<GenereEntity> findByName(String name);

    Optional<GenereEntity> findById(UUID id);

    List<GenereEntity> findAll();

    List<GenereEntity> findByIdIn(List<UUID> ids);

    List<GenereEntity> findByNameContainingIgnoreCase(String name);

}
