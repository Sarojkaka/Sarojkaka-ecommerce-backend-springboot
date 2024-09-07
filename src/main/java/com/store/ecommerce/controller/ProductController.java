package com.store.ecommerce.controller;


import com.store.ecommerce.dto.ProductDTO;
import com.store.ecommerce.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    @Value("${project.image}")
    private String imagePath;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;


    // Create a new product with image upload
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @Valid
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("image") MultipartFile image) throws IOException {
        logger.info("Received request to create product: {}", productDTO.getProductName());
        // Call service to handle product creation and image saving
        ProductDTO savedProduct = productService.createProduct(productDTO, image, imagePath);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        logger.info("Received request to get all products");
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        logger.info("Received request to get product by ID: {}", id);
        ProductDTO productDTO = productService.getProductById(id);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id,
                                                @Valid @ModelAttribute ProductDTO productDTO,
                                                @RequestParam("image") MultipartFile image) {
        logger.info("Received request to update product with ID: {}", id);
        ProductDTO updateProduct = productService.updateProduct(id, productDTO, image, imagePath);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        logger.info("Received request to delete product with ID: {}", id);
        String responseMessage = productService.deleteProduct(id);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}