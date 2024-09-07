package com.store.ecommerce.dto;

import com.store.ecommerce.entity.Category;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDTO {

    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private String description;
    private String date;
    private String imagePath;
    private Category category;
    private String categoryName;
    private Long categoryId;
}