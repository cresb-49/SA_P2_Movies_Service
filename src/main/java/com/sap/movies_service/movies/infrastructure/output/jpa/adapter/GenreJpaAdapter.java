package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.output.DeletingGenerePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.SaveGenerePort;
import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenreEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.mapper.GenreMapper;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.GenreEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GenreJpaAdapter implements FindingGenerePort, SaveGenerePort, DeletingGenerePort {

    private final GenreEntityRepository genreEntityRepository;
    private final GenreMapper genreMapper;

    @Override
    public Optional<Genre> findById(UUID id) {
        var entity = genreEntityRepository.findById(id);
        return entity.map(genreMapper::toDomain);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        var entity = genreEntityRepository.findByName(name);
        return entity.map(genreMapper::toDomain);
    }

    @Override
    public List<Genre> findLikeName(String name) {
        var entities = genreEntityRepository.findByNameContainingIgnoreCase(name);
        return entities.stream().map(genreMapper::toDomain).toList();
    }

    @Override
    public List<Genre> findAll() {
        var entities = genreEntityRepository.findAll();
        return entities.stream().map(genreMapper::toDomain).toList();
    }

    @Override
    public Genre save(Genre genre) {
        var entity = genreEntityRepository.save(new GenreEntity());
        return genreMapper.toDomain(entity);
    }

    @Override
    public void deleteById(UUID id) {
        genreEntityRepository.deleteById(id);
    }
}
