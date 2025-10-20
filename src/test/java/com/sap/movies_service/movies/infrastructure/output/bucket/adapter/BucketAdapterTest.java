

package com.sap.movies_service.movies.infrastructure.output.bucket.adapter;

import com.sap.movies_service.s3.infrastructure.input.port.BucketGatewayPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BucketAdapterTest {

    @Mock
    private BucketGatewayPort bucketGatewayPort;

    @InjectMocks
    private BucketAdapter bucketAdapter;

    private static final String BUCKET = "bucket-test";
    private static final String DIRECTORY = "movies";
    private static final String KEY = "movie-123.jpg";
    private static final byte[] DATA = {1, 2, 3};

    @Test
    @DisplayName("debe subir una imagen llamando al bucketGatewayPort")
    void uploadImage_shouldInvokeGateway() {
        // Arrange
        // Act
        bucketAdapter.uploadImage(BUCKET, DIRECTORY, KEY, DATA);

        // Assert
        verify(bucketGatewayPort, times(1)).uploadFileFromBytes(BUCKET, DIRECTORY, KEY, DATA);
    }

    @Test
    @DisplayName("debe eliminar una imagen llamando al bucketGatewayPort")
    void deleteImage_shouldInvokeGateway() {
        // Arrange
        // Act
        bucketAdapter.deleteImage(BUCKET, DIRECTORY, KEY);

        // Assert
        verify(bucketGatewayPort, times(1)).deleteFile(BUCKET, DIRECTORY, KEY);
    }
}