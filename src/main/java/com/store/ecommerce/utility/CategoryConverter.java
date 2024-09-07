package com.store.ecommerce.utility;

import com.store.ecommerce.dto.CategoryDTO;
import com.store.ecommerce.entity.Category;

public class CategoryConverter {

    // DTO to Entity conversion
    public static Category convertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        category.setDescription(categoryDTO.getDescription());
        return category;
    }

    // Entity to DTO conversion
    public static CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(category.getCategoryId());
        categoryDTO.setCategoryName(category.getCategoryName());
        categoryDTO.setDescription(category.getDescription());
        return categoryDTO;
    }
}
