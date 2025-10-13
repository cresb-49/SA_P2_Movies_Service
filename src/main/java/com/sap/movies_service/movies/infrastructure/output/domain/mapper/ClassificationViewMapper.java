package com.sap.movies_service.movies.infrastructure.output.domain.mapper;

import com.sap.movies_service.classifications.domain.Classification;
import com.sap.movies_service.movies.domain.ClassificationView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ClassificationViewMapper {

    public ClassificationView toView(Classification classification) {
        if (classification == null) {
            return null;
        }
        return new ClassificationView(
                classification.getId(),
                classification.getName(),
                classification.getDescription()
        );
    }

    public List<ClassificationView> toViewList(List<Classification> classifications) {
        return classifications.stream().map(this::toView).toList();
    }

}
