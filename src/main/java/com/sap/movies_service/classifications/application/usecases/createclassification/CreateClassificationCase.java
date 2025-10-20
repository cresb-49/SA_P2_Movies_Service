package com.sap.movies_service.classifications.application.usecases.createclassification;

import com.sap.movies_service.classifications.application.input.CreateClassificationCasePort;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.application.output.SaveClassificationPort;
import com.sap.movies_service.classifications.application.usecases.createclassification.dtos.CreateClassificationDTO;
import com.sap.movies_service.classifications.domain.Classification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CreateClassificationCase implements CreateClassificationCasePort {

    private final FindClassificationPort findClassificationPort;
    private final SaveClassificationPort saveClassificationPort;

    @Override
    public Classification create(CreateClassificationDTO createClassificationDTO) {

        findClassificationPort.findByNameIgnoreCase(createClassificationDTO.name()).ifPresent(
                c -> {
                    throw new IllegalArgumentException("La clasificación con nombre " + createClassificationDTO.name() + " ya existe");
                }
        );
        //Create classification
        Classification classification = new Classification(
                createClassificationDTO.name(),
                createClassificationDTO.description()
        );
        // Validate classification
        classification.validate();
        //Save classification
        return saveClassificationPort.save(classification);
    }
}
