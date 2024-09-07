package com.store.ecommerce.service;


import com.store.ecommerce.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {


    String createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    String deleteCategory(Long id);

    String updateCategory(Long id, CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(Long id);
}