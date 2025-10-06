package com.sap.movies_service.movies.application.output;

public interface SaveImagePort {
    void uploadImage(String bucketName, String keyName,  byte[] fileData);
}
