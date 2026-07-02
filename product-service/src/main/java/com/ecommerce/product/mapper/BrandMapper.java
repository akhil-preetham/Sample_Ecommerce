package com.ecommerce.product.mapper;

import com.ecommerce.product.entity.Brand;
import com.ecommerce.product.dto.BrandDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BrandMapper {

    BrandDTO toDTO(Brand entity);

    Brand toEntity(BrandDTO dto);
}
