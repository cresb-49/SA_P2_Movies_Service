package com.sap.movies_service.categories.application.output;

import com.sap.movies_service.categories.domain.Category;

public interface SaveCategoryPort {
    Category save(Category category);
}
