package com.ecommerce.product.service.impl;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.product.dto.CategoryDTO;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        log.info("Creating category: {}", categoryDTO.getName());
        
        Category category = categoryMapper.toEntity(categoryDTO);
        category.setId(UUIDUtil.generateUUID());
        category.setIsActive(true);
        
        Category savedCategory = categoryRepository.save(category);
        log.info("Category created with ID: {}", savedCategory.getId());
        
        return categoryMapper.toDTO(savedCategory);
    }

    @Override
    @Cacheable(value = "category", key = "#id", unless = "#result == null")
    public CategoryDTO getCategoryById(String id) {
        log.info("Fetching category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        return categoryMapper.toDTO(category);
    }

    @Override
    @Cacheable(value = "categories_paginated", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<CategoryDTO> getAllCategories(Pageable pageable) {
        log.info("Fetching all categories with pagination: page={}, size={}", 
            pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Category> categories = categoryRepository.findByIsActiveTrue(validPageable);
        
        return buildPaginationResponse(categories);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "categories_paginated"}, allEntries = true)
    public CategoryDTO updateCategory(String id, CategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        if (categoryDTO.getName() != null) {
            category.setName(categoryDTO.getName());
        }
        if (categoryDTO.getDescription() != null) {
            category.setDescription(categoryDTO.getDescription());
        }
        if (categoryDTO.getIsActive() != null) {
            category.setIsActive(categoryDTO.getIsActive());
        }
        
        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", id);
        
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "categories_paginated", "category"}, allEntries = true)
    public void deleteCategory(String id) {
        log.info("Deleting category with ID: {}", id);
        
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        
        categoryRepository.delete(category);
        log.info("Category deleted successfully with ID: {}", id);
    }

    private Pageable validatePageable(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if (pageSize > AppConstants.MAX_PAGE_SIZE) {
            pageSize = AppConstants.MAX_PAGE_SIZE;
        }
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
    }

    private PaginationResponse<CategoryDTO> buildPaginationResponse(Page<Category> page) {
        return PaginationResponse.<CategoryDTO>builder()
            .content(page.getContent().stream().map(categoryMapper::toDTO).toList())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }
}
