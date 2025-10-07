package com.sap.movies_service.movies.infrastructure.output.jpa.adapter;

import com.sap.movies_service.movies.application.output.DeletingGenerePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.SaveGenerePort;
import com.sap.movies_service.movies.domain.Genere;
import com.sap.movies_service.movies.infrastructure.output.jpa.entity.GenereEntity;
import com.sap.movies_service.movies.infrastructure.output.jpa.repository.GenereEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GenereJpaAdapter implements FindingGenerePort, SaveGenerePort, DeletingGenerePort {

    private final GenereEntityRepository genereEntityRepository;

    @Override
    public Optional<Genere> findById(UUID id) {
        var entity = genereEntityRepository.findById(id);
        return Optional.empty();
    }

    @Override
    public Optional<Genere> findByName(String name) {
        var entity = genereEntityRepository.findByName(name);
        return Optional.empty();
    }

    @Override
    public List<Genere> findLikeName(String name) {
        var entities = genereEntityRepository.findByNameContainingIgnoreCase(name);
        return List.of();
    }

    @Override
    public List<Genere> findAll() {
        var entities = genereEntityRepository.findAll();
        return List.of();
    }

    @Override
    public Genere save(Genere genere) {
        var entity = genereEntityRepository.save(new GenereEntity());
        return null;
    }

    @Override
    public void deleteById(UUID id) {
        genereEntityRepository.deleteById(id);
    }
}
