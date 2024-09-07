package com.store.ecommerce.utility;

import com.store.ecommerce.dto.ProductDTO;
import com.store.ecommerce.entity.Product;
import com.store.ecommerce.entity.Category;

    public class ProductConverter {

        public static ProductDTO convertToDTO(Product product) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setProductName(product.getProductName());
            productDTO.setPrice(product.getPrice());
            productDTO.setQuantity(product.getQuantity());
            productDTO.setDescription(product.getDescription());
            productDTO.setDate(product.getDate());
            productDTO.setImagePath(product.getImagePath());
            
            // Handle category mapping
            if (product.getCategory() != null) {
                productDTO.setCategoryId(product.getCategory().getCategoryId());
                productDTO.setCategoryName(product.getCategory().getCategoryName());
            } else {
                productDTO.setCategoryName("No Category");
            }

            return productDTO;
        }

        public static Product convertToEntity(ProductDTO productDTO, Category category) {
            Product product = new Product();
            product.setProductId(productDTO.getProductId());
            product.setProductName(productDTO.getProductName());
            product.setPrice(productDTO.getPrice());
            product.setQuantity(productDTO.getQuantity());
            product.setDescription(productDTO.getDescription());
            product.setDate(productDTO.getDate());
            product.setCategory(category);
            return product;
        }
    }
