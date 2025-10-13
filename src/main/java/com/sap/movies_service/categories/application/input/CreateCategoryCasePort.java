package com.sap.movies_service.categories.application.input;

import com.sap.movies_service.categories.domain.Category;

public interface CreateCategoryCasePort {
    Category create(String name);
}
