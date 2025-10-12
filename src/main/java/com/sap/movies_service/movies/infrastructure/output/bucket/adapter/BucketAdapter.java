package com.sap.movies_service.movies.infrastructure.output.bucket.adapter;

import com.sap.movies_service.s3.infrastructure.input.port.BucketGatewayPort;
import com.sap.movies_service.movies.application.output.DeletingImagePort;
import com.sap.movies_service.movies.application.output.SaveImagePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BucketAdapter implements SaveImagePort, DeletingImagePort {

    private final BucketGatewayPort bucketGatewayPort;

    @Override
    public void uploadImage(String bucketName, String directory, String keyName, byte[] fileData) {
        bucketGatewayPort.uploadFileFromBytes(bucketName, directory, keyName, fileData);
    }

    @Override
    public void deleteImage(String bucketName, String directory, String keyName) {
        bucketGatewayPort.deleteFile(bucketName, directory, keyName);
    }
}
