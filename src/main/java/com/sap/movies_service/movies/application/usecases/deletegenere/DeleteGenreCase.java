package com.sap.movies_service.movies.application.usecases.deletegenere;

import com.sap.movies_service.movies.application.input.DeleteGenerePort;
import com.sap.movies_service.movies.application.output.DeletingGenerePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeleteGenreCase implements DeleteGenerePort {

    private final DeletingGenerePort deletingGenerePort;
    private final FindingGenerePort findingGenerePort;
    private final FindingMoviePort findingMoviePort;

    @Override
    public void delete(UUID id) {
        findingGenerePort.findById(id).orElseThrow(() -> new RuntimeException("Genre not found"));
        if(findingMoviePort.existsByGenreId(id)) {
            throw new RuntimeException("Cannot delete genre with associated movies");
        }
        deletingGenerePort.deleteById(id);
    }
}
