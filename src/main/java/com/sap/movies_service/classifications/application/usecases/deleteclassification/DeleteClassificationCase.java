package com.sap.movies_service.classifications.application.usecases.deleteclassification;

import com.sap.movies_service.classifications.application.input.DeleteClassificationCasePort;
import com.sap.movies_service.classifications.application.output.DeleteClassificationPort;
import com.sap.movies_service.classifications.application.output.FindClassificationPort;
import com.sap.movies_service.classifications.application.output.MoviesWithClassificationPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeleteClassificationCase implements DeleteClassificationCasePort {

    private final DeleteClassificationPort deleteClassificationPort;
    private final FindClassificationPort findClassificationPort;
    private final MoviesWithClassificationPort moviesWithClassificationPort;

    @Override
    public void deleteById(UUID id) {
        var exists = findClassificationPort.existsById(id);
        if (!exists) {
            throw new IllegalArgumentException("Classification with id " + id + " not found");
        }
        var hasMovies = moviesWithClassificationPort.hasMoviesWithClassificationId(id);
        if (hasMovies) {
            throw new IllegalStateException("Classification with id " + id + " cannot be deleted because it is associated with movies");
        }
        deleteClassificationPort.deleteById(id);
    }
}
