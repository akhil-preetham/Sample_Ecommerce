package com.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {

    private String id;

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Image URL cannot be blank")
    @Size(max = 2000, message = "Image URL cannot exceed 2000 characters")
    private String imageUrl;

    @Size(max = 255, message = "Alt text cannot exceed 255 characters")
    private String altText;

    @Builder.Default
    private Boolean isDefault = false;

    @Builder.Default
    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder = 0;
}