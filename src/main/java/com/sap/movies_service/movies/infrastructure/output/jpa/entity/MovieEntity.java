package com.sap.movies_service.movies.infrastructure.output.jpa.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genere_id", nullable = false)
    private GenreEntity genre;

    @Column(nullable = false)
    private int duration;
    
    @Column(nullable = false)
    private String sinopsis;

    @Column(nullable = false)
    private String classification;

    @Column(nullable = false)
    private String director;

    @Column(nullable = false)
    private String casting;
    
    @Column(nullable = false)
    private String urlImage;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MovieEntity other = (MovieEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
