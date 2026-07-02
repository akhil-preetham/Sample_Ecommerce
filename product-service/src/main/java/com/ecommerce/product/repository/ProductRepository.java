package com.ecommerce.product.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends BaseRepository<Product, String> {

    Page<Product> findByIsActiveTrueAndCategoryId(String categoryId, Pageable pageable);

    Page<Product> findByIsActiveTrueAndBrandId(String brandId, Pageable pageable);

    Page<Product> findByIsActiveTrue(Pageable pageable);

    Page<Product> findByIsActiveTrueAndNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByIsActiveTrueAndCategoryIdAndBrandId(String categoryId, String brandId, Pageable pageable);

    long countByIsActiveTrue();
}
