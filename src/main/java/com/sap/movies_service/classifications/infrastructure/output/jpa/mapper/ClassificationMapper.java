package com.sap.movies_service.classifications.infrastructure.output.jpa.mapper;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.output.jpa.entity.ClassificationEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClassificationMapper {

    public Classification toDomain(ClassificationEntity entity) {
        if (entity == null) return null;
        return new Classification(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }

    public ClassificationEntity toEntity(Classification classification) {
        if (classification == null) return null;
        return new ClassificationEntity(
                classification.getId(),
                classification.getName(),
                classification.getDescription(),
                classification.getCreatedAt()
        );
    }
}
