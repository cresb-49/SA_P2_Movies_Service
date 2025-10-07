package com.sap.movies_service.movies.infrastructure.input.factory;

import com.sap.movies_service.movies.domain.Genere;
import com.sap.movies_service.movies.domain.Movie;
import com.sap.movies_service.movies.infrastructure.input.dtos.MovieResponseDTO;
import com.sap.movies_service.movies.infrastructure.input.mappers.GenereResponseMapper;
import com.sap.movies_service.movies.infrastructure.input.mappers.MovieResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MovieResponseFactory {

    private final GenereResponseMapper genereResponseMapper;
    private final MovieResponseMapper movieResponseMapper;

    public MovieResponseDTO createMovieResponseDTO(Movie movie, Genere genere){
        MovieResponseDTO movieResponseDTO = movieResponseMapper.toResponse(movie);
        movieResponseDTO.setGenere(genereResponseMapper.toResponse(genere));
        return movieResponseDTO;
    }

    public List<MovieResponseDTO> createMovieResponseDTOList(List<Movie> movies, List<Genere> generes){
        return movies.stream().map(movie -> {
            Genere genere = generes.stream()
                    .filter(g -> g.getId().equals(movie.getGenereId()))
                    .findFirst()
                    .orElse(null);
            return createMovieResponseDTO(movie, genere);
        }).toList();
    }
}
