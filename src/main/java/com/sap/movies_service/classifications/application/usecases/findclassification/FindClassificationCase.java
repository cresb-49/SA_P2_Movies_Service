package com.sap.movies_service.classifications.application.usecases.findclassification;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.classifications.application.input.FindClassificationCasePort;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.domain.Classification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class FindClassificationCase implements FindClassificationCasePort {

    private final FindClassificationPort findClassificationPort;


    @Override
    public boolean existsById(UUID id) {
        return findClassificationPort.existsById(id);
    }

    @Override
    public Classification findById(UUID id) {
        return findClassificationPort.findById(id)
                .orElseThrow(() -> new NotFoundException("La clasificación con id " + id + " no existe"));
    }

    @Override
    public List<Classification> findAll() {
        return findClassificationPort.findAll();
    }

    @Override
    public List<Classification> findAllById(List<UUID> ids) {
        return findClassificationPort.findAllById(ids);
    }
}
