package com.sap.movies_service.classifications.infrastructure.input.domain.gateway;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.input.domain.port.ClassificationGatewayPort;
import com.sap.movies_service.classifications.infrastructure.output.jpa.adapter.ClassificationJpaAdapter;
import com.sap.movies_service.classifications.infrastructure.output.jpa.repository.ClassificationEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class ClassificationGateway implements ClassificationGatewayPort {

    private final ClassificationJpaAdapter classificationJpaAdapter;

    @Override
    public boolean existsById(UUID id) {
        return classificationJpaAdapter.existsById(id);
    }

    @Override
    public Optional<Classification> findById(UUID id) {
        return classificationJpaAdapter.findById(id);
    }

    @Override
    public List<Classification> findByIds(List<UUID> ids) {
        return classificationJpaAdapter.findAllById(ids);
    }
}
