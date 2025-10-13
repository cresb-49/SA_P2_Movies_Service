package com.sap.movies_service.movies.application.usecases.updatemovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.application.output.DeletingImagePort;
import com.sap.movies_service.movies.application.output.FindingMoviePort;
import com.sap.movies_service.movies.application.output.SaveImagePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UpdateMovieCase implements UpdateMoviePort {

    @Value("${bucket.name}")
    private String bucketName;

    @Value("${bucket.directory}")
    private String bucketDirectory;

    @Value("${aws.region}")
    private String awsRegion;

    private final FindingMoviePort findingMoviePort;
    private final SaveImagePort saveImagePort;
    private final DeletingImagePort deletingImagePort;
    private final SaveMoviePort saveMoviePort;
    private final MovieFactory movieFactory;

    @Override
    public Movie update(UpdateMovieDTO updateMovieDTO) {
        // If the update movie DTO contains an image, we need to delete the old image and upload the new one
        // In other case, do nothing
        Movie movie = findingMoviePort.findById(updateMovieDTO.getId())
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        var updateImage = updateMovieDTO.getImage() != null && !updateMovieDTO.getImage().isEmpty();
        var now = System.currentTimeMillis();
        var oldUrlImage = movie.getUrlImage();
        Movie updatedMovie = updateMovieData(updateMovieDTO, movie, updateImage, now);
        //Validate the movie
        updatedMovie.validated();
        //Update image if needed
        if (updateImage) {
            uploadImageToS3(updateMovieDTO.getImage(), movie, now);
            deleteImageFromS3(oldUrlImage);
        }
        var savedMovie = saveMoviePort.save(updatedMovie);
        return movieFactory.movieWithAllRelations(savedMovie);
    }

    private Movie updateMovieData(UpdateMovieDTO updateMovieDTO, Movie movie, boolean updateImage, Long now) {
        var urlImage = movie.getUrlImage();
        if (updateImage) {
            urlImage = parseImageData(updateMovieDTO.getImage(), movie, now);
        }
        movie.update(
                updateMovieDTO.getTitle(),
                updateMovieDTO.getDuration(),
                updateMovieDTO.getSinopsis(),
                updateMovieDTO.getClassificationId(),
                updateMovieDTO.getDirector(),
                updateMovieDTO.getCasting(),
                urlImage
        );
        return movie;
    }

    private String parseImageData(MultipartFile image, Movie movie, Long timestamp) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalStateException("Image must have a name");
        }
        // Name of the image file is a UUID of the movie + extension
        var imageName = "movie-" + timestamp.toString();
        // Get Original extension
        var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //Set the urlImage of the movie
        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + bucketDirectory + "/" + imageName + "." + extension;
    }

    private void uploadImageToS3(MultipartFile image, Movie movie, Long timestamp) {
        try {
            var originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalStateException("Image must have a name");
            }
            var imageName = "movie-" + timestamp.toString();
            var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            var key = imageName + "." + extension;
            byte[] bytes = image.getBytes();
            saveImagePort.uploadImage(bucketName, bucketDirectory, key, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void deleteImageFromS3(String urlImage) {
        try {
            var key = urlImage.substring(urlImage.lastIndexOf("/") + 1);
            System.out.println("Deleting image with key: " + key);
            deletingImagePort.deleteImage(bucketName, bucketDirectory, key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
