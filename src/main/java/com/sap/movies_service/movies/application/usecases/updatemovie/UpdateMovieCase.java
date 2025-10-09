package com.sap.movies_service.movies.application.usecases.updatemovie;

import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.application.output.*;
import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
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
    private final FindingGenerePort findingGenerePort;

    @Override
    public Movie update(UpdateMovieDTO updateMovieDTO) {
        // If the update movie DTO contains an image, we need to delete the old image and upload the new one
        // In other case, do nothing
        Movie movie = findingMoviePort.findById(updateMovieDTO.getId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        var updateImage = updateMovieDTO.getImage() != null && !updateMovieDTO.getImage().isEmpty();
        var now = System.currentTimeMillis();
        Movie updatedMovie = updateMovieData(updateMovieDTO, movie, updateImage, now);
        //Validate the movie
        updatedMovie.validated();
        //Update image if needed
        if(updateImage) {
            deleteImageFromS3(movie);
            uploadImageToS3(updateMovieDTO.getImage(), movie, now);
        }
        return saveMoviePort.save(updatedMovie);
    }

    private Movie updateMovieData(UpdateMovieDTO updateMovieDTO, Movie movie, boolean updateImage, Long now) {
        var genre = updateMovieDTO.getGenereId() != null ?
                findingGenerePort.findById(updateMovieDTO.getGenereId())
                        .orElseThrow(() -> new RuntimeException("Genere with id " + updateMovieDTO.getGenereId() + " does not exist"))
                : movie.getGenre();
        movie.update(
                updateMovieDTO.getTitle(),
                genre,
                updateMovieDTO.getDuration(),
                updateMovieDTO.getSinopsis()
        );
        if(updateImage) {
            var urlImage = parseImageData(updateMovieDTO.getImage(), movie, now);
            movie.setUrlImage(urlImage);
        }
        return movie;
    }

    private String parseImageData(MultipartFile image, Movie movie, Long timestamp) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new RuntimeException("Image must have a name");
        }
        // Name of the image file is a UUID of the movie + extension
        var imageName = movie.getId().toString() + "-" + timestamp.toString();
        // Get Original extension
        var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //Set the urlImage of the movie
        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + bucketDirectory + "/" + imageName + "." + extension;
    }

    private void uploadImageToS3(MultipartFile image, Movie movie,Long timestamp) {
        try {
            var originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new RuntimeException("Image must have a name");
            }
            var imageName = movie.getId().toString() + "-" + timestamp.toString();
            var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            var key = imageName + "." + extension;
            byte[] bytes = image.getBytes();
            saveImagePort.uploadImage(bucketName, bucketDirectory, key, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void deleteImageFromS3(Movie movie) {
        try {
            var urlImage = movie.getUrlImage();
            if (urlImage == null || urlImage.isEmpty()) {
                throw new RuntimeException("Movie does not have an image to delete");
            }
            // Extract the key from the urlImage
            var key = urlImage.substring(urlImage.lastIndexOf("/") + 1);
            deletingImagePort.deleteImage(bucketName, bucketDirectory, key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
