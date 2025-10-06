package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;

public interface MovieEntityRepository extends JpaRepository<MovieEntity, UUID> {
    Optional<MovieEntity> findById(UUID id);

    List<MovieEntity> findAll();

    List<MovieEntity> findByGenereId(UUID genereId);

    List<MovieEntity> findByIdIn(List<UUID> ids);

    List<MovieEntity> findByTitleContainingIgnoreCase(String title);
}
