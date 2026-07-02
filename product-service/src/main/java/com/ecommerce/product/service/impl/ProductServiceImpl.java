package com.ecommerce.product.service.impl;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.product.dto.ProductDTO;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO createProduct(ProductDTO productDTO) {
        log.info("Creating product: {}", productDTO.getName());
        
        Product product = productMapper.toEntity(productDTO);
        product.setId(UUIDUtil.generateUUID());
        product.setIsActive(true);
        
        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        
        return productMapper.toDTO(savedProduct);
    }

    @Override
    @Cacheable(value = "product", key = "#id", unless = "#result == null")
    public ProductDTO getProductById(String id) {
        log.info("Fetching product with ID: {}", id);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        return productMapper.toDTO(product);
    }

    @Override
    @Cacheable(value = "products", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<ProductDTO> getAllProducts(Pageable pageable) {
        log.info("Fetching all products with pagination: page={}, size={}", 
            pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Product> products = productRepository.findByIsActiveTrue(validPageable);
        
        return buildPaginationResponse(products);
    }

    @Override
    @Cacheable(value = "product_search", key = "#query + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<ProductDTO> searchProducts(String query, Pageable pageable) {
        log.info("Searching products with query: {} page={}, size={}", 
            query, pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Product> products = productRepository.findByIsActiveTrueAndNameContainingIgnoreCase(query, validPageable);
        
        return buildPaginationResponse(products);
    }

    @Override
    @Cacheable(value = "product_category", key = "#categoryId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<ProductDTO> getProductsByCategory(String categoryId, Pageable pageable) {
        log.info("Fetching products by category: {} page={}, size={}", 
            categoryId, pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Product> products = productRepository.findByIsActiveTrueAndCategoryId(categoryId, validPageable);
        
        return buildPaginationResponse(products);
    }

    @Override
    @Cacheable(value = "product_brand", key = "#brandId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<ProductDTO> getProductsByBrand(String brandId, Pageable pageable) {
        log.info("Fetching products by brand: {} page={}, size={}", 
            brandId, pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Product> products = productRepository.findByIsActiveTrueAndBrandId(brandId, validPageable);
        
        return buildPaginationResponse(products);
    }

    @Override
    @Cacheable(value = "product_category_brand", key = "#categoryId + '_' + #brandId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<ProductDTO> getProductsByCategoryAndBrand(String categoryId, String brandId, Pageable pageable) {
        log.info("Fetching products by category: {} and brand: {} page={}, size={}", 
            categoryId, brandId, pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Product> products = productRepository.findByIsActiveTrueAndCategoryIdAndBrandId(categoryId, brandId, validPageable);
        
        return buildPaginationResponse(products);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product_search", "product_category", "product_brand", "product_category_brand"}, allEntries = true)
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }
        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }
        if (productDTO.getCategoryId() != null) {
            product.setCategoryId(productDTO.getCategoryId());
        }
        if (productDTO.getBrandId() != null) {
            product.setBrandId(productDTO.getBrandId());
        }
        if (productDTO.getBasePrice() != null) {
            product.setBasePrice(productDTO.getBasePrice());
        }
        if (productDTO.getRating() != null) {
            product.setRating(productDTO.getRating());
        }
        if (productDTO.getReviewCount() != null) {
            product.setReviewCount(productDTO.getReviewCount());
        }
        if (productDTO.getIsActive() != null) {
            product.setIsActive(productDTO.getIsActive());
        }
        if (productDTO.getSku() != null) {
            product.setSku(productDTO.getSku());
        }
        if (productDTO.getStock() != null) {
            product.setStock(productDTO.getStock());
        }
        
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", id);
        
        return productMapper.toDTO(updatedProduct);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product_search", "product_category", "product_brand", "product_category_brand", "product"}, allEntries = true)
    public void deleteProduct(String id) {
        log.info("Deleting product with ID: {}", id);
        
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        
        productRepository.delete(product);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    @Cacheable(value = "product_count")
    public long getTotalProductCount() {
        log.info("Fetching total product count");
        return productRepository.countByIsActiveTrue();
    }

    private Pageable validatePageable(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if (pageSize > AppConstants.MAX_PAGE_SIZE) {
            pageSize = AppConstants.MAX_PAGE_SIZE;
        }
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
    }

    private PaginationResponse<ProductDTO> buildPaginationResponse(Page<Product> page) {
        return PaginationResponse.<ProductDTO>builder()
            .content(page.getContent().stream().map(productMapper::toDTO).toList())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }
}
