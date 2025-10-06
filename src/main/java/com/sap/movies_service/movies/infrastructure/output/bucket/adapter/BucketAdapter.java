package com.sap.movies_service.movies.infrastructure.output.bucket.adapter;

import com.sap.movies_service.config.infrastructure.input.port.BucketGatewayPort;
import com.sap.movies_service.movies.application.output.SaveImagePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BucketAdapter implements SaveImagePort {

    private final BucketGatewayPort bucketGatewayPort;

    @Override
    public void uploadImage(String bucketName, String keyName, byte[] fileData) {
        bucketGatewayPort.uploadFileFromBytes(bucketName, keyName, fileData);
    }
}
