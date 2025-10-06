package com.sap.movies_service.config.infrastructure.input;

import com.sap.movies_service.config.infrastructure.input.port.BucketGatewayPort;
import com.sap.movies_service.config.infrastructure.output.adapter.S3ServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Component
@AllArgsConstructor
@Transactional
public class BucketGateway implements BucketGatewayPort {

    private final S3ServicePort s3ServicePort;

    @Override
    public void uploadFileFromBytes(String bucketName, String keyName, byte[] fileData) {
        s3ServicePort.uploadFileFromBytes(bucketName, keyName, fileData);
    }

    @Override
    public void uploadFileFromFile(String bucketName, String keyName, File file) {
        s3ServicePort.uploadFileFromFile(bucketName, keyName, file);
    }

    @Override
    public byte[] downloadFile(String bucketName, String keyName) throws IOException {
        return s3ServicePort.downloadFile(bucketName, keyName);
    }
}
