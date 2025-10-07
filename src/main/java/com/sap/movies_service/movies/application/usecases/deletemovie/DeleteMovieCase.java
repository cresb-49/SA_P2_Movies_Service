package com.sap.movies_service.movies.application.usecases.deletemovie;

import com.sap.movies_service.movies.application.input.DeleteMoviePort;
import com.sap.movies_service.movies.application.output.DeletingMoviePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeleteMovieCase implements DeleteMoviePort {

    private final DeletingMoviePort deletingMoviePort;

    @Override
    public void delete(UUID id) {
        deletingMoviePort.deleteMovieById(id);
    }
}
