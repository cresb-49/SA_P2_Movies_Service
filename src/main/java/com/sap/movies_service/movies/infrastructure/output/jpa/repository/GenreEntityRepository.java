package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GenreEntityRepository extends JpaRepository<GenreEntity, UUID> {

    Optional<GenreEntity> findByName(String name);

    Optional<GenreEntity> findById(UUID id);

    List<GenreEntity> findAll();

    List<GenreEntity> findByIdIn(List<UUID> ids);

    List<GenreEntity> findByNameContainingIgnoreCase(String name);

}
