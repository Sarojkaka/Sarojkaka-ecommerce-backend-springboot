package com.store.ecommerce.service.serviceImpl;

import com.store.ecommerce.dto.CategoryDTO;
import com.store.ecommerce.entity.Category;
import com.store.ecommerce.service.CategoryService;
import com.store.ecommerce.exception.ResourceNotFoundException;
import com.store.ecommerce.repository.CategoryRepository;
import com.store.ecommerce.utility.CategoryConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String createCategory(CategoryDTO categoryDTO) {
        logger.info("Creating new category: {}", categoryDTO.getCategoryName());
        Category category = CategoryConverter.convertToEntity(categoryDTO);
        categoryRepository.save(category);
        logger.info("Category created successfully with ID: {}", category.getCategoryId());
        return String.format("Category '%s' created successfully with ID: %d", category.getCategoryName(), category.getCategoryId());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        logger.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            logger.warn("No categories found in the system.");
            throw new ResourceNotFoundException("No categories found.");
        }
        logger.info("Returning {} categories", categories.size());
        return categories.stream()
                .map(CategoryConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String deleteCategory(Long id) {
        logger.info("Attempting to delete category with ID: {}", id);
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            categoryRepository.deleteById(id);
            logger.info("Category with ID {} deleted successfully", id);
            return String.format("Category with ID %d deleted successfully.", id);
        } else {
            logger.error("Category with ID {} not found for deletion.", id);
            throw new ResourceNotFoundException("Category with ID " + id + " not found.");
        }
    }

    @Override
    public String updateCategory(Long id, CategoryDTO categoryDTO) {
        logger.info("Attempting to update category with ID: {}", id);

        // Fetch the existing category from the database
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with ID {} not found for update.", id);
                    return new ResourceNotFoundException("Category with ID " + id + " not found.");
                });

        // Check if the category name and description have actually changed
        boolean isUpdated = false;

        if (!existingCategory.getCategoryName().equals(categoryDTO.getCategoryName())) {
            logger.info("Updating category name from '{}' to '{}'",
                    existingCategory.getCategoryName(), categoryDTO.getCategoryName());
            existingCategory.setCategoryName(categoryDTO.getCategoryName());
            isUpdated = true;
        }

        if (!existingCategory.getDescription().equals(categoryDTO.getDescription())) {
            logger.info("Updating category description from '{}' to '{}'",
                    existingCategory.getDescription(), categoryDTO.getDescription());
            existingCategory.setDescription(categoryDTO.getDescription());
            isUpdated = true;
        }

        // If no fields were updated, return a message indicating that
        if (!isUpdated) {
            logger.info("No changes detected for category with ID: {}", id);
            return String.format("No changes made to category with ID: %d", id);
        }

        // Save only if there were updates
        categoryRepository.save(existingCategory);
        logger.info("Category with ID {} updated successfully.", id);
        return String.format("Category with ID %d updated successfully.", id);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        logger.info("Fetching category by ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Category with ID {} not found.", id);
                    return new ResourceNotFoundException("Category with ID " + id + " not found.");
                });

        logger.info("Returning category with ID: {}", id);
        return CategoryConverter.convertToDTO(category);
    }
}
