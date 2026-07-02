package com.ecommerce.product.service;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.product.dto.CategoryDTO;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryById(String id);

    PaginationResponse<CategoryDTO> getAllCategories(Pageable pageable);

    CategoryDTO updateCategory(String id, CategoryDTO categoryDTO);

    void deleteCategory(String id);
}
