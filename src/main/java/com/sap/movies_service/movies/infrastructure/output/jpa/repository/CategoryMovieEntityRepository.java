package com.sap.movies_service.movies.infrastructure.output.jpa.repository;

import com.sap.movies_service.movies.infrastructure.output.jpa.entity.CategoryMovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CategoryMovieEntityRepository extends JpaRepository<CategoryMovieEntity, UUID> {

    @Query("""
               select cm.categoryId
               from CategoryMovieEntity cm
               where cm.movieId = :movieId
            """)
    List<UUID> findCategoryIdsByMovieId(@Param("movieId") UUID movieId);

    @Modifying
    @Query("""
               delete from CategoryMovieEntity cm
               where cm.movieId = :movieId
                 and cm.categoryId in :categoryIds
            """)
    int deleteByMovieIdAndCategoryIdIn(@Param("movieId") UUID movieId,
                                       @Param("categoryIds") Collection<UUID> categoryIds);

    @Modifying
    @Query("""
               delete from CategoryMovieEntity cm
               where cm.movieId = :movieId
            """)
    int deleteByMovieId(@Param("movieId") UUID movieId);
}