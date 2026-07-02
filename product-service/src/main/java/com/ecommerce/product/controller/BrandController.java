package com.ecommerce.product.controller;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.product.dto.BrandDTO;
import com.ecommerce.product.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Brand Management", description = "Brand operations")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "Get all brands", description = "Retrieve all active brands with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brands retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ResponseWrapper<PaginationResponse<BrandDTO>>> getAllBrands(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching all brands: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<BrandDTO> brands = brandService.getAllBrands(pageable);

        return ResponseEntity.ok(ResponseWrapper.success(brands, "Brands retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get brand by ID", description = "Retrieve a single brand by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ResponseWrapper<BrandDTO>> getBrandById(
            @Parameter(description = "Brand ID")
            @PathVariable String id) {

        log.info("Fetching brand with ID: {}", id);
        BrandDTO brand = brandService.getBrandById(id);

        return ResponseEntity.ok(ResponseWrapper.success(brand, "Brand retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create brand", description = "Create a new brand (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Brand created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid brand data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Brand already exists")
    })
    public ResponseEntity<ResponseWrapper<BrandDTO>> createBrand(
            @Valid @RequestBody BrandDTO brandDTO) {

        log.info("Creating brand: {}", brandDTO.getName());
        BrandDTO createdBrand = brandService.createBrand(brandDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(createdBrand, "Brand created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update brand", description = "Update an existing brand (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Brand updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid brand data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<ResponseWrapper<BrandDTO>> updateBrand(
            @Parameter(description = "Brand ID")
            @PathVariable String id,
            @Valid @RequestBody BrandDTO brandDTO) {

        log.info("Updating brand with ID: {}", id);
        BrandDTO updatedBrand = brandService.updateBrand(id, brandDTO);

        return ResponseEntity.ok(ResponseWrapper.success(updatedBrand, "Brand updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete brand", description = "Delete a brand (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Brand deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Brand not found")
    })
    public ResponseEntity<Void> deleteBrand(
            @Parameter(description = "Brand ID")
            @PathVariable String id) {

        log.info("Deleting brand with ID: {}", id);
        brandService.deleteBrand(id);

        return ResponseEntity.noContent().build();
    }
}
