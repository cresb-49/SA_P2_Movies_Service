package com.sap.movies_service.config.infrastructure.input.port;

import java.io.File;
import java.io.IOException;

public interface BucketGatewayPort {
    void uploadFileFromBytes(String bucketName, String keyName, byte[] fileData);

    void uploadFileFromFile(String bucketName, String keyName, File file);

    byte[] downloadFile(String bucketName, String keyName) throws IOException;
}
