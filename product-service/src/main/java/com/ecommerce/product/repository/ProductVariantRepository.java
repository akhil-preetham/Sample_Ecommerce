package com.ecommerce.product.repository;

import com.ecommerce.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {

    List<ProductVariant> findByProductId(String productId);
}
