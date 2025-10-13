package com.sap.movies_service.classifications.infrastructure.output.domain.adapter;

import com.sap.movies_service.classifications.application.output.MoviesWithClassificationPort;
import com.sap.movies_service.movies.infrastructure.input.domain.port.MovieGatewayPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MovieClassificationAdapter implements MoviesWithClassificationPort {

    private final MovieGatewayPort movieGatewayPort;

    @Override
    public boolean hasMoviesWithClassificationId(UUID id) {
        return movieGatewayPort.hasMoviesWithClassificationId(id);
    }
}
