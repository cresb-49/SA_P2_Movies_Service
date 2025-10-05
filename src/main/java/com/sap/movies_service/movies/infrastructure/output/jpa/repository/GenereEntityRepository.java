package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenereEntity;

public interface GenereEntityRepository extends JpaRepository<GenereEntity, UUID> {

    Optional<GenereEntity> findByName(String name);

    Optional<GenereEntity> findById(UUID id);

    List<GenereEntity> findAll();

    List<GenereEntity> findByIdIn(List<UUID> ids);
    
}
