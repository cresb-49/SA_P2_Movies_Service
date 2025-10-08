package com.sap.movies_service.movies.application.usecases.createmovie;

import com.sap.movies_service.movies.application.input.CreateMoviePort;
import com.sap.movies_service.movies.application.output.FindingGenerePort;
import com.sap.movies_service.movies.application.output.SaveImagePort;
import com.sap.movies_service.movies.application.output.SaveMoviePort;
import com.sap.movies_service.movies.application.usecases.createmovie.dtos.CreateMovieDTO;
import com.sap.movies_service.movies.domain.Genre;
import com.sap.movies_service.movies.domain.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
public class CreateMovieCase implements CreateMoviePort {

    private final FindingGenerePort findingGenerePort;
    private final SaveMoviePort saveMoviePort;
    private final SaveImagePort saveImagePort;

    @Override
    public Movie create(CreateMovieDTO createMovieDTO) throws IOException {
        if (createMovieDTO.getImage().isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }
        if (!List.of("image/png", "image/jpg", "image/jpeg").contains(createMovieDTO.getImage().getContentType())) {
            throw new IllegalArgumentException("Image must be png, jpg or jpeg");
        }
        // Check if the genere exists
        Genre genre = findingGenerePort.findById(createMovieDTO.getGenereId())
                .orElseThrow(() -> new IllegalArgumentException("Genere with id " + createMovieDTO.getGenereId() + " does not exist"));
        // Create a Movie domain object
        Movie movie = new Movie(
                createMovieDTO.getTitle(),
                genre,
                createMovieDTO.getDuration(),
                createMovieDTO.getSinopsis()
        );
        // Process image
        var urlImage = parseImageData(createMovieDTO.getImage(), movie);
        // Set the urlImage of the movie
        movie.setUrlImage(urlImage);
        // Validate the movie
        movie.validated();
        // Upload the image to S3
        this.uploadImageToS3(createMovieDTO.getImage(), movie);
        // Save the movie to the database
        return saveMoviePort.save(movie);
    }

    private String parseImageData(MultipartFile image, Movie movie) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("Image must have a name");
        }
        // Name of the image file is a UUID of the movie + extension
        var imageName = movie.getId().toString();
        // Get Original extension
        var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //Set the urlImage of the movie
        var bucketName = System.getenv("AWS_BUCKET_NAME");
        var region = System.getenv("AWS_REGION");
        var directory = System.getenv("AWS_DIRECTORY");
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + directory + "/" + imageName + "." + extension;
    }

    private void uploadImageToS3(MultipartFile image, Movie movie) throws IOException {
        var bucketName = System.getenv("AWS_BUCKET_NAME");
        var imageName = movie.getId().toString();
        byte[] bytes = image.getBytes();
        saveImagePort.uploadImage(bucketName,imageName,bytes);
    }
}
