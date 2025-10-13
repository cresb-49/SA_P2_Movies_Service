package com.sap.movies_service.categories.application.input;

import com.sap.movies_service.categories.application.usecases.updatecategorycase.dtos.UpdateCategoryDTO;
import com.sap.movies_service.categories.domain.Category;

public interface UpdateCategoryCasePort {
    Category update(UpdateCategoryDTO updateCategoryDTO);
}
