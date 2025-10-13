package com.sap.movies_service.classifications.infrastructure.output.jpa.adapter;

import com.sap.movies_service.classifications.application.output.DeleteClassificationPort;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.application.output.SaveClassificationPort;
import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.output.jpa.mapper.ClassificationMapper;
import com.sap.movies_service.classifications.infrastructure.output.jpa.repository.ClassificationEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClassificationJpaAdapter implements DeleteClassificationPort, FindClassificationPort, SaveClassificationPort {

    private final ClassificationEntityRepository repository;
    private final ClassificationMapper mapper;

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Classification> findById(UUID id) {
        var entity = repository.findById(id);
        return entity.map(mapper::toDomain);
    }

    @Override
    public Optional<Classification> findByNameIgnoreCase(String name) {
        var entity = repository.findByNameIgnoreCase(name);
        return entity.map(mapper::toDomain);
    }

    @Override
    public List<Classification> findAll() {
        var entities = repository.findAll();
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Classification> findAllById(List<UUID> ids) {
        var entities = repository.findAllById(ids);
        return entities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Classification save(Classification classification) {
        var entity = mapper.toEntity(classification);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
