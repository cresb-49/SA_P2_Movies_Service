package com.sap.movies_service.classifications.infrastructure.input.web.mapper;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.classifications.infrastructure.input.web.dtos.ClassificationResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ClassificationResponseMapper {

    public ClassificationResponseDTO toDTO(Classification classification) {
        return new ClassificationResponseDTO(
                classification.getId(),
                classification.getName(),
                classification.getDescription(),
                classification.getCreatedAt()
        );
    }

    public List<ClassificationResponseDTO> toDTOList(List<Classification> classifications) {
        return classifications.stream().map(this::toDTO).toList();
    }
}
