package com.sap.movies_service.categories.application.usecases.deletecategory;

import com.sap.common_lib.exception.NotFoundException;
import com.sap.movies_service.categories.application.input.DeleteCategoryCasePort;
import com.sap.movies_service.categories.application.output.DeleteCategoryPort;
import com.sap.movies_service.categories.application.output.FindCategoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class DeleteCategoryCase implements DeleteCategoryCasePort {

    private final FindCategoryPort findCategoryPort;
    private final DeleteCategoryPort deleteCategoryPort;

    @Override
    public void deleteById(UUID id) {
        // Check if category exists
        findCategoryPort.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Category with id " + id + " not found")
                );
        deleteCategoryPort.deleteById(id);
    }
}
