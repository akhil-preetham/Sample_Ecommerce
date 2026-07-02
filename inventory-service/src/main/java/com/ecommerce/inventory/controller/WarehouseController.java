package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.WarehouseDTO;
import com.ecommerce.inventory.service.WarehouseService;
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
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse Management", description = "APIs for warehouse management")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    @Operation(
        summary = "Get all warehouses",
        description = "Retrieve list of all warehouses",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Warehouses retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        log.info("Getting all warehouses");
        
        List<WarehouseDTO> warehouses = warehouseService.getAllWarehouses();
        
        return ResponseEntity.ok(warehouses);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get warehouse by ID",
        description = "Retrieve warehouse details by ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Warehouse retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Warehouse not found")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable String id) {
        log.info("Getting warehouse: {}", id);
        
        WarehouseDTO warehouse = warehouseService.getWarehouseById(id);
        
        return ResponseEntity.ok(warehouse);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create warehouse",
        description = "Create a new warehouse (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "Warehouse created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        log.info("Creating warehouse: {}", warehouseDTO.getName());
        
        WarehouseDTO created = warehouseService.createWarehouse(warehouseDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update warehouse",
        description = "Update warehouse details (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Warehouse updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    @ApiResponse(responseCode = "404", description = "Warehouse not found")
    public ResponseEntity<WarehouseDTO> updateWarehouse(
            @PathVariable String id,
            @Valid @RequestBody WarehouseDTO warehouseDTO) {
        log.info("Updating warehouse: {}", id);
        
        WarehouseDTO updated = warehouseService.updateWarehouse(id, warehouseDTO);
        
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete warehouse",
        description = "Delete a warehouse (Admin only)",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Warehouse deleted successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required")
    @ApiResponse(responseCode = "404", description = "Warehouse not found")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable String id) {
        log.info("Deleting warehouse: {}", id);
        
        warehouseService.deleteWarehouse(id);
        
        return ResponseEntity.noContent().build();
    }
}
