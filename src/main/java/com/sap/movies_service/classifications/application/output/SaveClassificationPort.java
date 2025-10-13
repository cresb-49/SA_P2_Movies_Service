package com.sap.movies_service.classifications.application.output;

import com.sap.movies_service.classifications.domain.Classification;

public interface SaveClassificationPort {
    Classification save(Classification classification);
}
