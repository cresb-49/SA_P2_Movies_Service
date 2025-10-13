package com.sap.movies_service.classifications.application.input;

import com.sap.movies_service.classifications.application.usecases.createclassification.dtos.CreateClassificationDTO;
import com.sap.movies_service.classifications.domain.Classification;

public interface CreateClassificationCasePort {
    Classification create(CreateClassificationDTO createClassificationDTO);
}
