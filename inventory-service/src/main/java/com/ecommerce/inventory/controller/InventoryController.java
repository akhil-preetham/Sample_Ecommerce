package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.*;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.inventory.service.ReportingService;
import com.ecommerce.inventory.service.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
        summary = "Check product availability",
        description = "Check if a product variant has sufficient stock",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Availability check completed")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Object> checkAvailability(@Valid @RequestBody CheckAvailabilityRequest request) {
        log.info("Checking availability for variant: {}", request.getProductVariantId());
        
        boolean available = inventoryService.checkAvailability(request);
        
        return ResponseEntity.ok()
            .body(new Object() {
                public final boolean available = available;
                public final String productVariantId = request.getProductVariantId();
                public final Long requestedQuantity = request.getQuantity();
            });
    }

    @PostMapping("/reserve")
    @Operation(
        summary = "Reserve stock for order",
        description = "Reserve stock quantity for a pending order",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Stock reserved successfully")
    @ApiResponse(responseCode = "400", description = "Insufficient stock or invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public ResponseEntity<Object> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
        log.info("Reserving stock - Order: {}", request.getOrderId());
        
        String reservationId = inventoryService.reserveStock(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new Object() {
                public final String reservationId = reservationId;
                public final String message = "Stock reserved successfully";
            });
    }

    @PostMapping("/release")
    @Operation(
        summary = "Release reserved stock",
        description = "Release previously reserved stock when order is cancelled",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Stock released successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Item or reservation not found")
    public ResponseEntity<Object> releaseStock(@Valid @RequestBody ReleaseStockRequest request) {
        log.info("Releasing stock - Order: {}", request.getOrderId());
        
        inventoryService.releaseStock(request);
        
        return ResponseEntity.ok()
            .body(new Object() {
                public final String message = "Stock released successfully";
                public final String orderId = request.getOrderId();
            });
    }

    @GetMapping("/status/{variantId}")
    @Operation(
        summary = "Get inventory status",
        description = "Get current stock status for a product variant",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Variant not found")
    public ResponseEntity<StockStatusResponse> getInventoryStatus(
            @PathVariable String variantId) {
        log.info("Getting inventory status for variant: {}", variantId);
        
        StockStatusResponse response = inventoryService.getInventoryStatus(variantId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get low stock items",
        description = "Get all items with stock below reorder level (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Low stock items retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    public ResponseEntity<List<Object>> getLowStockItems() {
        log.info("Getting low stock items");
        
        List<Object> lowStockItems = reportingService.getLowStockItems();
        
        return ResponseEntity.ok(lowStockItems);
    }

    @GetMapping("/movements/{itemId}")
    @Operation(
        summary = "Get stock movement history",
        description = "Get all stock movements for an inventory item",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Movement history retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Item not found")
    public ResponseEntity<List<Object>> getMovementHistory(
            @PathVariable String itemId) {
        log.info("Getting movement history for item: {}", itemId);
        
        List<Object> movements = stockMovementService.getMovementHistory(itemId);
        
        return ResponseEntity.ok(movements);
    }
}
