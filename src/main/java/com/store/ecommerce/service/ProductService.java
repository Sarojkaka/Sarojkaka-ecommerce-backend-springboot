package com.store.ecommerce.service;

import com.store.ecommerce.dto.ProductDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO, MultipartFile file,String imagePath);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile file,String imagePath);

    String deleteProduct(Long id);
}
