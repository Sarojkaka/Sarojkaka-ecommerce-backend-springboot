package com.store.ecommerce.service.serviceImpl;

import com.store.ecommerce.dto.ProductDTO;
import com.store.ecommerce.entity.Category;
import com.store.ecommerce.entity.Product;
import com.store.ecommerce.exception.ResourceNotFoundException;
import com.store.ecommerce.repository.CategoryRepository;
import com.store.ecommerce.repository.ProductRepository;
import com.store.ecommerce.service.ProductService;
import com.store.ecommerce.utility.ProductConverter;
import com.store.ecommerce.utility.FileUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Value("${project.image}")
    private String imagePath;  // Externalized image path

    @Autowired
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtility fileUtility;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, FileUtility fileUtility) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.fileUtility = fileUtility;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile file, String imagePath) {
        logger.info("Creating new product: {}", productDTO.getProductName());

        // Fetch the category by ID from the DTO
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + productDTO.getCategoryId()));

        // Convert ProductDTO to Product entity
        Product product = ProductConverter.convertToEntity(productDTO, category);

        // Save image file if present
        if (file != null && !file.isEmpty()) {
            String fileName = fileUtility.saveImage(file, imagePath);  // Using FileUtility for file operations
            product.setImagePath(fileName);  // Set the saved image path
        }

        // Save the product to the database
        productRepository.save(product);
        logger.info("Product created successfully with ID: {}", product.getProductId());

        // Convert the saved product entity back to a DTO and return it
        return ProductConverter.convertToDTO(product);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile file, String imagePath) {
        logger.info("Updating product with ID: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found."));

        updateProductFields(existingProduct, productDTO);  // Modularized field updates

        if (file != null && !file.isEmpty()) {
            logger.info("Updating product image for product with ID: {}", id);
            String fileName = fileUtility.saveImage(file, imagePath);
            existingProduct.setImagePath(fileName);
        }

        productRepository.save(existingProduct);
        logger.info("Product with ID {} updated successfully.", id);

        // Convert the updated product to DTO and return it
        return ProductConverter.convertToDTO(existingProduct);
    }


    @Override
    public List<ProductDTO> getAllProducts() {
        logger.info("Fetching all products");
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            logger.warn("No products found in the system.");
            throw new ResourceNotFoundException("No products found.");
        }

        return products.stream()
                .map(ProductConverter::convertToDTO)
                .collect(Collectors.toList());
    }



    @Override
    public ProductDTO getProductById(Long id) {
        logger.info("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found."));

        return ProductConverter.convertToDTO(product);
    }

    @Override
    public String deleteProduct(Long id) {
        logger.info("Attempting to delete product with ID: {}", id);

        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            logger.info("Product with ID {} deleted successfully", id);
            return String.format("Product with ID %d deleted successfully.", id);
        } else {
            logger.error("Product with ID {} not found for deletion.", id);
            throw new ResourceNotFoundException("Product with ID " + id + " not found.");
        }
    }

    // Helper method for updating product fields
    private void updateProductFields(Product existingProduct, ProductDTO productDTO) {
        if (!existingProduct.getProductName().equals(productDTO.getProductName())) {
            existingProduct.setProductName(productDTO.getProductName());
        }
        if (!existingProduct.getDescription().equals(productDTO.getDescription())) {
            existingProduct.setDescription(productDTO.getDescription());
        }
        if (!existingProduct.getPrice().equals(productDTO.getPrice())) {
            existingProduct.setPrice(productDTO.getPrice());
        }
        // Add other field updates as needed
    }
}