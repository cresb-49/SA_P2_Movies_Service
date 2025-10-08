package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieEntityRepository extends JpaRepository<MovieEntity, UUID> {

    Optional<MovieEntity> findById(UUID id);

    List<MovieEntity> findByIdIn(List<UUID> ids);

    Page<MovieEntity> findByGenre_Id(UUID genreId, Pageable pageable);

    Page<MovieEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
