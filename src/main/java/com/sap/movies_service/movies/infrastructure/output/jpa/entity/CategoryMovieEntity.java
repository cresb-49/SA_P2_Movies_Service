package com.sap.movies_service.movies.infrastructure.output.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "category_movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMovieEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID categoryId;
    @Column(nullable = false)
    private UUID movieId;
}