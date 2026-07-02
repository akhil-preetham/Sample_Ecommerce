package com.ecommerce.product.repository;

import com.ecommerce.common.repository.BaseRepository;
import com.ecommerce.product.entity.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category, String> {

    Optional<Category> findByName(String name);

    @Cacheable(value = "categories")
    List<Category> findByIsActiveTrue();

    Page<Category> findByIsActiveTrue(Pageable pageable);
}
