package com.ecommerce.product.service.impl;

import com.ecommerce.common.constant.AppConstants;
import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.util.UUIDUtil;
import com.ecommerce.product.dto.BrandDTO;
import com.ecommerce.product.entity.Brand;
import com.ecommerce.product.mapper.BrandMapper;
import com.ecommerce.product.repository.BrandRepository;
import com.ecommerce.product.service.BrandService;
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
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    @CacheEvict(value = "brands", allEntries = true)
    public BrandDTO createBrand(BrandDTO brandDTO) {
        log.info("Creating brand: {}", brandDTO.getName());
        
        Brand brand = brandMapper.toEntity(brandDTO);
        brand.setId(UUIDUtil.generateUUID());
        brand.setIsActive(true);
        
        Brand savedBrand = brandRepository.save(brand);
        log.info("Brand created with ID: {}", savedBrand.getId());
        
        return brandMapper.toDTO(savedBrand);
    }

    @Override
    @Cacheable(value = "brand", key = "#id", unless = "#result == null")
    public BrandDTO getBrandById(String id) {
        log.info("Fetching brand with ID: {}", id);
        
        Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
        
        return brandMapper.toDTO(brand);
    }

    @Override
    @Cacheable(value = "brands_paginated", key = "#pageable.pageNumber + '_' + #pageable.pageSize")
    public PaginationResponse<BrandDTO> getAllBrands(Pageable pageable) {
        log.info("Fetching all brands with pagination: page={}, size={}", 
            pageable.getPageNumber(), pageable.getPageSize());
        
        Pageable validPageable = validatePageable(pageable);
        Page<Brand> brands = brandRepository.findByIsActiveTrue(validPageable);
        
        return buildPaginationResponse(brands);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"brands", "brands_paginated"}, allEntries = true)
    public BrandDTO updateBrand(String id, BrandDTO brandDTO) {
        log.info("Updating brand with ID: {}", id);
        
        Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
        
        if (brandDTO.getName() != null) {
            brand.setName(brandDTO.getName());
        }
        if (brandDTO.getDescription() != null) {
            brand.setDescription(brandDTO.getDescription());
        }
        if (brandDTO.getIsActive() != null) {
            brand.setIsActive(brandDTO.getIsActive());
        }
        
        Brand updatedBrand = brandRepository.save(brand);
        log.info("Brand updated successfully with ID: {}", id);
        
        return brandMapper.toDTO(updatedBrand);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"brands", "brands_paginated", "brand"}, allEntries = true)
    public void deleteBrand(String id) {
        log.info("Deleting brand with ID: {}", id);
        
        Brand brand = brandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + id));
        
        brandRepository.delete(brand);
        log.info("Brand deleted successfully with ID: {}", id);
    }

    private Pageable validatePageable(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        if (pageSize > AppConstants.MAX_PAGE_SIZE) {
            pageSize = AppConstants.MAX_PAGE_SIZE;
        }
        return PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());
    }

    private PaginationResponse<BrandDTO> buildPaginationResponse(Page<Brand> page) {
        return PaginationResponse.<BrandDTO>builder()
            .content(page.getContent().stream().map(brandMapper::toDTO).toList())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }
}
