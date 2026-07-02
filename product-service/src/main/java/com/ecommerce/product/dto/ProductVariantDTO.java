package com.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {

    private String id;

    @NotBlank(message = "Product ID cannot be blank")
    private String productId;

    @NotBlank(message = "Variant name cannot be blank")
    @Size(min = 2, max = 255, message = "Variant name must be between 2 and 255 characters")
    private String variantName;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @Size(max = 100, message = "SKU cannot exceed 100 characters")
    private String sku;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock cannot be negative")
    private Long stock;
}
