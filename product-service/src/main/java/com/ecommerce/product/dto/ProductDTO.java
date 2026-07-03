package com.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String id;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 3, max = 255, message = "Product name must be between 3 and 255 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Category ID cannot be blank")
    private String categoryId;

    @NotBlank(message = "Brand ID cannot be blank")
    private String brandId;

    @NotNull(message = "Base price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5")
    private Double rating;

    @Min(value = 0, message = "Review count cannot be negative")
    private Long reviewCount;

    @Builder.Default
    private Boolean isActive = true;

    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;

    @Min(value = 0, message = "Stock cannot be negative")
    @Builder.Default
    private Long stock = 0L;

    private List<ProductVariantDTO> variants;

    private List<ProductImageDTO> images;
}
