package com.sap.movies_service.movies.infrastructure.output.domain.adapter;

import com.sap.movies_service.classifications.infrastructure.input.domain.port.ClassificationGatewayPort;
import com.sap.movies_service.movies.application.output.FindingClassificationPort;
import com.sap.movies_service.movies.domain.ClassificationView;
import com.sap.movies_service.movies.infrastructure.output.domain.mapper.ClassificationViewMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ClassificationMovieAdapter implements FindingClassificationPort {

    private final ClassificationGatewayPort classificationGatewayPort;
    private final ClassificationViewMapper mapper;

    @Override
    public boolean existsById(UUID id) {
        return classificationGatewayPort.existsById(id);
    }

    @Override
    public ClassificationView findById(UUID id) {
        var classification = classificationGatewayPort.findById(id).orElse(null);
        return mapper.toView(classification);
    }
}
