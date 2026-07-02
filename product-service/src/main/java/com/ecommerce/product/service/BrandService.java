package com.ecommerce.product.service;

import com.ecommerce.common.dto.PaginationResponse;
import com.ecommerce.product.dto.BrandDTO;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    BrandDTO createBrand(BrandDTO brandDTO);

    BrandDTO getBrandById(String id);

    PaginationResponse<BrandDTO> getAllBrands(Pageable pageable);

    BrandDTO updateBrand(String id, BrandDTO brandDTO);

    void deleteBrand(String id);
}
