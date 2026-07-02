package com.ecommerce.inventory.controller;

import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.inventory.dto.*;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.inventory.service.ReportingService;
import com.ecommerce.inventory.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory Management", description = "APIs for inventory and stock management")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ReportingService reportingService;
    private final StockMovementService stockMovementService;

    @PostMapping("/check-availability")
    @Operation(summary = "Check product availability", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<AvailabilityResponse>> checkAvailability(
            @Valid @RequestBody CheckAvailabilityRequest request) {
        log.info("Checking availability for variant: {}", request.getProductVariantId());

        boolean available = inventoryService.checkAvailability(request);

        AvailabilityResponse response = AvailabilityResponse.builder()
            .available(available)
            .productVariantId(request.getProductVariantId())
            .requestedQuantity(request.getQuantity())
            .build();

        return ResponseEntity.ok(ResponseWrapper.success(response, "Availability check completed"));
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock for order", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<ReservationResponse>> reserveStock(
            @Valid @RequestBody ReserveStockRequest request) {
        log.info("Reserving stock - Order: {}", request.getOrderId());

        String reservationId = inventoryService.reserveStock(request);

        ReservationResponse response = ReservationResponse.builder()
            .reservationId(reservationId)
            .message("Stock reserved successfully")
            .build();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ResponseWrapper.success(response, "Stock reserved successfully"));
    }

    @PostMapping("/release")
    @Operation(summary = "Release reserved stock", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<ReleaseResponse>> releaseStock(
            @Valid @RequestBody ReleaseStockRequest request) {
        log.info("Releasing stock - Order: {}", request.getOrderId());

        inventoryService.releaseStock(request);

        ReleaseResponse response = ReleaseResponse.builder()
            .message("Stock released successfully")
            .orderId(request.getOrderId())
            .build();

        return ResponseEntity.ok(ResponseWrapper.success(response, "Stock released successfully"));
    }

    @GetMapping("/status/{variantId}")
    @Operation(summary = "Get inventory status", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<StockStatusResponse>> getInventoryStatus(
            @PathVariable String variantId) {
        log.info("Getting inventory status for variant: {}", variantId);

        StockStatusResponse response = inventoryService.getInventoryStatus(variantId);

        return ResponseEntity.ok(ResponseWrapper.success(response, "Status retrieved successfully"));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Get low stock items (Admin only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<List<LowStockItemDTO>>> getLowStockItems() {
        log.info("Getting low stock items");

        List<LowStockItemDTO> lowStockItems = reportingService.getLowStockItems();

        return ResponseEntity.ok(ResponseWrapper.success(lowStockItems, "Low stock items retrieved"));
    }

    @GetMapping("/movements/{itemId}")
    @Operation(summary = "Get stock movement history", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseWrapper<List<StockMovementDTO>>> getMovementHistory(
            @PathVariable String itemId) {
        log.info("Getting movement history for item: {}", itemId);

        List<StockMovementDTO> movements = stockMovementService.getMovementHistory(itemId);

        return ResponseEntity.ok(ResponseWrapper.success(movements, "Movement history retrieved"));
    }
}
