package com.sap.movies_service.categories.application.usecases.createcategory;

import com.sap.common_lib.exception.EntityAlreadyExistsException;
import com.sap.movies_service.categories.application.input.CreateCategoryCasePort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.application.output.SaveCategoryPort;
import com.sap.movies_service.categories.domain.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CreateCategoryCase implements CreateCategoryCasePort {

    private final FindCategoryPort findCategoryPort;
    private final SaveCategoryPort saveCategoryPort;

    @Override
    public Category create(String name) {
        // find with insensitive name
        findCategoryPort.findByNameInsensitive(name).ifPresent(
                c -> {
                    throw new EntityAlreadyExistsException("La categoría con nombre " + name + " ya existe");
                }
        );
        // Create category
        Category category = new Category(name);
        // Validate category
        category.validate();
        // Save category
        return saveCategoryPort.save(category);
    }
}
