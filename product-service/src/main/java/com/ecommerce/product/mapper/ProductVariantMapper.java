package com.ecommerce.product.mapper;

import com.ecommerce.product.entity.ProductVariant;
import com.ecommerce.product.dto.ProductVariantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductVariantMapper {

    ProductVariantDTO toDTO(ProductVariant entity);

    ProductVariant toEntity(ProductVariantDTO dto);
}
