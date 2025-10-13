package com.sap.movies_service.categories.application.usecases.updatecategorycase;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.input.UpdateCategoryCasePort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import com.sap.movies_service.categories.application.output.SaveCategoryPort;
import com.sap.movies_service.categories.application.usecases.updatecategorycase.dtos.UpdateCategoryDTO;
import com.sap.movies_service.categories.domain.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UpdateCategoryCase implements UpdateCategoryCasePort {

    private final FindCategoryPort findCategoryPort;
    private final SaveCategoryPort saveCategoryPort;

    @Override
    public Category update(UpdateCategoryDTO updateCategoryDTO) {
        // Find category by id
        Category category = findCategoryPort.findById(updateCategoryDTO.id()).orElseThrow(
                () -> new NotFoundException("Category with id " + updateCategoryDTO.id() + " not found")
        );
        // Find category by new name
        findCategoryPort.findByNameInsensitive(updateCategoryDTO.name(), updateCategoryDTO.id()
        ).ifPresent(
                c -> {
                    throw new IllegalArgumentException("Category with name " + updateCategoryDTO.name() + " already exists");
                }
        );
        // Update category
        category.update(updateCategoryDTO.name());
        // Validate category
        category.validate();
        // Return category
        return saveCategoryPort.save(category);
    }
}
