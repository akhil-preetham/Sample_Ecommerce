package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Wishlist item")
public class WishlistItemDTO {

    @Schema(description = "Wishlist item ID", example = "wi-123")
    private String id;

    @Schema(description = "Product variant ID", example = "var-456")
    private String productVariantId;

    @Schema(description = "Item added timestamp")
    private LocalDateTime addedAt;
}
