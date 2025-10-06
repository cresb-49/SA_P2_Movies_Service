package com.sap.movies_service.movies.application.usecases.creategenere;

import com.sap.movies_service.movies.application.input.CreateGenerePort;
import com.sap.movies_service.movies.application.input.FindGenerePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.SaveGenerePort;
import com.sap.movies_service.movies.application.usecases.creategenere.dtos.CreateGenereDTO;
import com.sap.movies_service.movies.domain.Genere;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class CreateGenereCase implements CreateGenerePort {

    private final FindingGenerePort findingGenerePort;
    private final SaveGenerePort saveGenerePort;

    @Override
    public Genere create(CreateGenereDTO createGenereDTO) {
        findingGenerePort.findByName(createGenereDTO.getName())
                .ifPresent(g -> {
                    throw new IllegalArgumentException("Genere with name " + createGenereDTO.getName() + " already exists");
                });
        Genere genere = new Genere(createGenereDTO.getName());
        return saveGenerePort.save(genere);
    }
}
