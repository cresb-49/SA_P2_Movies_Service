package com.sap.movies_service.movies.application.output;

public interface DeletingImagePort {
    void deleteImage(String bucketName, String directory, String keyName);
}
