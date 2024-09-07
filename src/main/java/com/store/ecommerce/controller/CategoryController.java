package com.store.ecommerce.controller;

import com.store.ecommerce.dto.CategoryDTO;
import com.store.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    // Create a new category
    @PostMapping
    public ResponseEntity<String> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("Received request to create category: {}", categoryDTO.getCategoryName());
        String responseMessage = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        logger.info("Received request to get all categories");
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long id) {
        logger.info("Received request to get category by ID: {}", id);
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryDTO categoryDTO) {
        logger.info("Received request to update category with ID: {}", id);
        String responseMessage = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    // Delete category
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        logger.info("Received request to delete category with ID: {}", id);
        String responseMessage = categoryService.deleteCategory(id);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
