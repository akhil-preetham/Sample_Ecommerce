package com.ecommerce.product.controller;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.product.dto.CategoryDTO;
import com.ecommerce.product.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "Category operations")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all active categories with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ResponseWrapper<PaginationResponse<CategoryDTO>>> getAllCategories(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") int size) {

        log.info("Fetching all categories: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<CategoryDTO> categories = categoryService.getAllCategories(pageable);

        return ResponseEntity.ok(ResponseWrapper.success(categories, "Categories retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a single category by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ResponseWrapper<CategoryDTO>> getCategoryById(
            @Parameter(description = "Category ID")
            @PathVariable String id) {

        log.info("Fetching category with ID: {}", id);
        CategoryDTO category = categoryService.getCategoryById(id);

        return ResponseEntity.ok(ResponseWrapper.success(category, "Category retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create category", description = "Create a new category (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Category already exists")
    })
    public ResponseEntity<ResponseWrapper<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO) {

        log.info("Creating category: {}", categoryDTO.getName());
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(createdCategory, "Category created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update category", description = "Update an existing category (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid category data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<ResponseWrapper<CategoryDTO>> updateCategory(
            @Parameter(description = "Category ID")
            @PathVariable String id,
            @Valid @RequestBody CategoryDTO categoryDTO) {

        log.info("Updating category with ID: {}", id);
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);

        return ResponseEntity.ok(ResponseWrapper.success(updatedCategory, "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete category", description = "Delete a category (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID")
            @PathVariable String id) {

        log.info("Deleting category with ID: {}", id);
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
