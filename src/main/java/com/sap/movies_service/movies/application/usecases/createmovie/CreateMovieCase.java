package com.sap.movies_service.movies.application.usecases.createmovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.input.CreateMoviePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.SaveImagePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.application.usecases.createmovie.dtos.CreateMovieDTO;
import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CreateMovieCase implements CreateMoviePort {

    @Value("${bucket.name}")
    private String bucketName;

    @Value("${bucket.directory}")
    private String bucketDirectory;

    @Value("${aws.region}")
    private String awsRegion;

    private final FindingGenerePort findingGenerePort;
    private final SaveMoviePort saveMoviePort;
    private final SaveImagePort saveImagePort;

    @Override
    public Movie create(CreateMovieDTO createMovieDTO) {
        if (createMovieDTO.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }
        if (!List.of("image/png", "image/jpg", "image/jpeg").contains(createMovieDTO.getImage().getContentType())) {
            throw new IllegalArgumentException("Image must be png, jpg or jpeg");
        }
        // Check if the genere exists
        Genre genre = findingGenerePort.findById(createMovieDTO.getGenereId())
                .orElseThrow(() -> new NotFoundException("Genere with id " + createMovieDTO.getGenereId() + " does not exist"));
        // Create a Movie domain object
        Movie movie = new Movie(
                createMovieDTO.getTitle(),
                genre,
                createMovieDTO.getDuration(),
                createMovieDTO.getSinopsis(),
                createMovieDTO.getClassification(),
                createMovieDTO.getDirector(),
                createMovieDTO.getCasting()
        );
        // Generation timestamp
        var now = System.currentTimeMillis();
        // Process image
        var urlImage = parseImageData(createMovieDTO.getImage(), movie, now);
        // Set the urlImage of the movie
        movie.setUrlImage(urlImage);
        // Validate the movie
        movie.validated();
        // Upload the image to S3
        this.uploadImageToS3(createMovieDTO.getImage(), movie, now);
        // Save the movie to the database
        return saveMoviePort.save(movie);
    }

    private String parseImageData(MultipartFile image, Movie movie,Long timestamp) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalStateException("Image must have a name");
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
                throw new IllegalStateException("Image must have a name");
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
}
