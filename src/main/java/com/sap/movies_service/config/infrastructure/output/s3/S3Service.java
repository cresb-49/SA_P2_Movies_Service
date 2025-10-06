package com.sap.movies_service.config.infrastructure.output.s3;

import com.sap.movies_service.config.infrastructure.output.adapter.S3ServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class S3Service implements S3ServicePort {
    private final S3Client s3Client;

    @Override
    public void uploadFileFromBytes(String bucketName, String keyName, byte[] fileData) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(fileData));
    }

    @Override
    public void uploadFileFromFile(String bucketName, String keyName, File file) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        s3Client.putObject(putObjectRequest, software.amazon.awssdk.core.sync.RequestBody.fromFile(file));
    }

    @Override
    public byte[] downloadFile(String bucketName, String keyName) throws IOException {
        return s3Client.getObject(builder -> builder.bucket(bucketName).key(keyName))
                .readAllBytes();
    }
}
