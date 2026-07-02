package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to update cart item quantity")
public class UpdateCartItemRequest {

    @Schema(description = "New quantity", example = "5")
    private Long quantity;
}
