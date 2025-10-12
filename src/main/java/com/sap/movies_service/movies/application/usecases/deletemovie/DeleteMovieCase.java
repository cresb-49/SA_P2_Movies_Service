package com.sap.movies_service.movies.application.usecases.deletemovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.input.DeleteMoviePort;
import com.sap.movies_service.movies.application.output.DeletingImagePort;
import com.sap.movies_service.movies.application.output.DeletingMoviePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeleteMovieCase implements DeleteMoviePort {

    @Value("${bucket.name}")
    private String bucketName;
    @Value("${bucket.directory}")
    private String bucketDirectory;
    @Value("${aws.region}")
    private String awsRegion;

    private final FindingMoviePort findingMoviePort;
    private final DeletingMoviePort deletingMoviePort;
    private final DeletingImagePort deletingImagePort;

    @Override
    public void delete(UUID id) {
        Movie movie = findingMoviePort.findById(id).orElseThrow(
                () -> new NotFoundException("Movie with id " + id + " does not exist")
        );
        deletingMoviePort.deleteMovieById(id);
        deleteImageFromS3(movie);
    }

    private void deleteImageFromS3(Movie movie) {
        try {
            var urlImage = movie.getUrlImage();
            if (urlImage == null || urlImage.isEmpty()) {
                throw new IllegalStateException("Movie does not have an image to delete");
            }
            // Extract the key from the urlImage
            var key = urlImage.substring(urlImage.lastIndexOf("/") + 1);
            deletingImagePort.deleteImage(bucketName, bucketDirectory, key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
