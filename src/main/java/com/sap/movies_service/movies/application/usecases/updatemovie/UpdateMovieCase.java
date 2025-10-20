package com.sap.movies_service.movies.application.usecases.updatemovie;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.movies.application.factory.MovieFactory;
import com.sap.movies_service.movies.application.input.UpdateMoviePort;
import com.sap.movies_service.movies.application.output.*;
import com.sap.movies_service.movies.application.usecases.updatemovie.dtos.UpdateMovieDTO;
import com.sap.movies_service.movies.domain.CategoryMovie;
import com.sap.movies_service.movies.domain.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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
    private final FindingCategoriesPort findingCategoriesPort;
    private final FindingClassificationPort findingClassificationPort;
    private final ModifyCategoriesMoviePort modifyCategoriesMoviePort;
    private final FindingCategoriesMoviePort findingCategoriesMoviePort;
    private final SaveCategoriesMoviePort saveCategoriesMoviePort;

    private final MovieFactory movieFactory;

    @Override
    public Movie update(UpdateMovieDTO updateMovieDTO) {
        // If the update movie DTO contains an image, we need to delete the old image and upload the new one
        // In other case, do nothing
        Movie movie = findingMoviePort.findById(updateMovieDTO.getId())
                .orElseThrow(() -> new NotFoundException("La película con ID " + updateMovieDTO.getId() + " no existe"));
        // Verify if classificationId exists
        var existsClassification = findingClassificationPort.existsById(updateMovieDTO.getClassificationId());
        if (!existsClassification) {
            throw new NotFoundException("La clasificación seleccionada no existe");
        }
        // Verify if categoriesId exists
        var categories = findingCategoriesPort.findAllById(updateMovieDTO.getCategoryIds());
        if (categories.size() != updateMovieDTO.getCategoryIds().size()) {
            var diffCount = updateMovieDTO.getCategoryIds().size() - categories.size();
            throw new NotFoundException(diffCount + " categorías seleccionadas no existen");
        }
        // Validate image if exists
        var updateImage = updateMovieDTO.getImage() != null && !updateMovieDTO.getImage().isEmpty();
        var now = System.currentTimeMillis();
        var oldUrlImage = movie.getUrlImage();
        Movie updatedMovie = updateMovieData(updateMovieDTO, movie, updateImage, now);
        //Validate the movie
        updatedMovie.validated();
        var savedMovie = saveMoviePort.save(updatedMovie);
        // Update categories if needed
        updateCategories(movie, updateMovieDTO.getCategoryIds());
        //Update image if needed
        if (updateImage) {
            uploadImageToS3(updateMovieDTO.getImage(), movie, now);
            deleteImageFromS3(oldUrlImage);
        }
        return movieFactory.movieWithAllRelations(savedMovie);
    }

    /**
     * Update the categories of the movie
     * @param movie
     * @param newCategoryIds
     */
    private void updateCategories(Movie movie, List<UUID> newCategoryIds) {
        var currentCategories = findingCategoriesMoviePort.findCategoryIdsByMovieId(movie.getId());

        var toRemove = new HashSet<>(currentCategories); toRemove.removeAll(newCategoryIds);
        var toAdd = new HashSet<>(newCategoryIds); toAdd.removeAll(currentCategories);
        if (!toRemove.isEmpty()) {
            modifyCategoriesMoviePort.deleteByMovieIdAndCategoryIdIn(movie.getId(), toRemove.stream().toList());
        }
        if (!toAdd.isEmpty()) {
            List<CategoryMovie> newCategoriesMovies = toAdd.stream()
                    .map(categoryId -> new CategoryMovie(categoryId,movie.getId()))
                    .toList();
            saveCategoriesMoviePort.saveCategoriesMovie(newCategoriesMovies);
        }
    }

    /**
     * Update the movie data
     * @param updateMovieDTO
     * @param movie
     * @param updateImage
     * @param now
     * @return
     */
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

    /**
     * Parse the image data to get the urlImage
     * @param image
     * @param movie
     * @param timestamp
     * @return
     */
    private String parseImageData(MultipartFile image, Movie movie, Long timestamp) {
        var originalFilename = image.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalStateException("La imagen debe tener un nombre");
        }
        // Name of the image file is a UUID of the movie + extension
        var imageName = "movie-" + timestamp.toString();
        // Get Original extension
        var extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //Set the urlImage of the movie
        return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + bucketDirectory + "/" + imageName + "." + extension;
    }

    /**
     * Upload the image to S3
     * @param image
     * @param movie
     * @param timestamp
     */
    private void uploadImageToS3(MultipartFile image, Movie movie, Long timestamp) {
        try {
            var originalFilename = image.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalStateException("La imagen debe tener un nombre");
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

    /**
     * Delete the image from S3
     * @param urlImage
     */
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
