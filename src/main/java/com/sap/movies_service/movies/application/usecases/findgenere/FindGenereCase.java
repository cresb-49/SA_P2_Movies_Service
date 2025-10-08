package com.sap.movies_service.movies.application.usecases.findgenere;

import com.sap.movies_service.movies.application.input.FindGenerePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FindGenereCase implements FindGenerePort {

    private final FindingGenerePort findingGenerePort;

    @Override
    public Genre findById(UUID id) {
        return findingGenerePort.findById(id).orElse(null);
    }

    @Override
    public List<Genre> findLikeTitle(String title) {
        return findingGenerePort.findLikeName(title);
    }

    @Override
    public List<Genre> findAll() {
        return findingGenerePort.findAll();
    }
}
