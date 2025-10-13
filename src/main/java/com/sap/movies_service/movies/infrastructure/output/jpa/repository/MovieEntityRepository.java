package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface MovieEntityRepository extends JpaRepository<MovieEntity, UUID>, JpaSpecificationExecutor<MovieEntity> {

    List<MovieEntity> findByIdIn(List<UUID> ids);

    Page<MovieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    boolean existsByClassificationId(UUID id);
}
