package com.ecommerce.product.service;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.product.dto.ProductDTO;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO getProductById(String id);

    PaginationResponse<ProductDTO> getAllProducts(Pageable pageable);

    PaginationResponse<ProductDTO> searchProducts(String query, Pageable pageable);

    PaginationResponse<ProductDTO> getProductsByCategory(String categoryId, Pageable pageable);

    PaginationResponse<ProductDTO> getProductsByBrand(String brandId, Pageable pageable);

    PaginationResponse<ProductDTO> getProductsByCategoryAndBrand(String categoryId, String brandId, Pageable pageable);

    ProductDTO updateProduct(String id, ProductDTO productDTO);

    void deleteProduct(String id);

    long getTotalProductCount();
}
