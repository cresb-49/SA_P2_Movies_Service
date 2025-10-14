package com.sap.movies_service.movies.infrastructure.input.web.mappers;

import com.sap.common_lib.dto.response.movie.ClassificationViewResponseDTO;
import com.sap.movies_service.movies.domain.ClassificationView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ClassificationViewResponseMapper {

    public ClassificationViewResponseDTO toResponse(ClassificationView classificationView) {
        if (classificationView == null) {
            return null;
        }
        return new ClassificationViewResponseDTO(
                classificationView.id(),
                classificationView.name(),
                classificationView.description()
        );
    }

    public List<ClassificationViewResponseDTO> toResponseList(List<ClassificationView> classificationViews) {
        if (classificationViews == null) {
            return null;
        }
        return classificationViews.stream().map(this::toResponse).toList();
    }
}
