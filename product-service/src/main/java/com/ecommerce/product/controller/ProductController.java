package com.ecommerce.product.controller;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.dto.ResponseWrapper;
import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.service.ProductService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "Product operations")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve all active products with pagination, filters, and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    public ResponseEntity<ResponseWrapper<PaginationResponse<ProductDTO>>> getAllProducts(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort by field (name, basePrice, rating, createdAt)")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)")
            @RequestParam(defaultValue = "desc") Sort.Direction direction,
            @Parameter(description = "Filter by category ID")
            @RequestParam(required = false) String categoryId,
            @Parameter(description = "Filter by brand ID")
            @RequestParam(required = false) String brandId,
            @Parameter(description = "Min price filter")
            @RequestParam(required = false) String minPrice,
            @Parameter(description = "Max price filter")
            @RequestParam(required = false) String maxPrice) {

        log.info("Fetching products: page={}, size={}, categoryId={}, brandId={}", page, size, categoryId, brandId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        PaginationResponse<ProductDTO> products;

        if (categoryId != null && brandId != null) {
            products = productService.getProductsByCategoryAndBrand(categoryId, brandId, pageable);
        } else if (categoryId != null) {
            products = productService.getProductsByCategory(categoryId, pageable);
        } else if (brandId != null) {
            products = productService.getProductsByBrand(brandId, pageable);
        } else {
            products = productService.getAllProducts(pageable);
        }

        return ResponseEntity.ok(ResponseWrapper.success(products, "Products retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieve a single product by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ResponseWrapper<ProductDTO>> getProductById(
            @Parameter(description = "Product ID")
            @PathVariable String id) {

        log.info("Fetching product with ID: {}", id);
        ProductDTO product = productService.getProductById(id);

        return ResponseEntity.ok(ResponseWrapper.success(product, "Product retrieved successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search products", description = "Search products by name and description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<ResponseWrapper<PaginationResponse<ProductDTO>>> searchProducts(
            @Parameter(description = "Search query")
            @RequestParam String q,
            @Parameter(description = "Page number (0-based)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size (max 100)")
            @RequestParam(defaultValue = "20") int size) {

        log.info("Searching products with query: {}", q);
        Pageable pageable = PageRequest.of(page, size);
        PaginationResponse<ProductDTO> results = productService.searchProducts(q, pageable);

        return ResponseEntity.ok(ResponseWrapper.success(results, "Products searched successfully"));
    }

    @GetMapping("/suggestions")
    @Operation(summary = "Get product suggestions", description = "Get autocomplete suggestions for product search")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Suggestions retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<ResponseWrapper<PaginationResponse<ProductDTO>>> getProductSuggestions(
            @Parameter(description = "Search query")
            @RequestParam String q) {

        log.info("Fetching product suggestions for query: {}", q);
        Pageable pageable = PageRequest.of(0, 10);
        PaginationResponse<ProductDTO> suggestions = productService.searchProducts(q, pageable);

        return ResponseEntity.ok(ResponseWrapper.success(suggestions, "Suggestions retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Create product", description = "Create a new product (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "409", description = "Product already exists")
    })
    public ResponseEntity<ResponseWrapper<ProductDTO>> createProduct(
            @Valid @RequestBody ProductDTO productDTO) {

        log.info("Creating product: {}", productDTO.getName());
        ProductDTO createdProduct = productService.createProduct(productDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseWrapper.success(createdProduct, "Product created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid product data"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ResponseWrapper<ProductDTO>> updateProduct(
            @Parameter(description = "Product ID")
            @PathVariable String id,
            @Valid @RequestBody ProductDTO productDTO) {

        log.info("Updating product with ID: {}", id);
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);

        return ResponseEntity.ok(ResponseWrapper.success(updatedProduct, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Product ID")
            @PathVariable String id) {

        log.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
