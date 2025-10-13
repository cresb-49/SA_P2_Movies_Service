package com.sap.movies_service.movies.infrastructure.input.domain.gateway;

import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.input.domain.port.MovieGatewayPort;
import com.sap.movies_service.movies.infrastructure.output.jpa.adapter.MovieJpaAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
@Transactional(readOnly = true)
public class MovieGateway implements MovieGatewayPort {

    private final MovieJpaAdapter movieJpaAdapter;

    @Override
    public boolean hasMoviesWithClassificationId(UUID id) {
        return movieJpaAdapter.hasMoviesWithClassificationId(id);
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return movieJpaAdapter.findById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return movieJpaAdapter.existsById(id);
    }

    @Override
    public List<Movie> findByIds(List<UUID> ids) {
        return movieJpaAdapter.findByIdsIn(ids);
    }
}
