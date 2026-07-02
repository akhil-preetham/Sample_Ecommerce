package com.ecommerce.product.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.product.entity.Brand;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends BaseRepository<Brand, String> {

    Optional<Brand> findByName(String name);

    @Cacheable(value = "brands")
    List<Brand> findByIsActiveTrue();

    Page<Brand> findByIsActiveTrue(Pageable pageable);
}
