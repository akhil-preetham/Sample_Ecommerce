package com.ecommerce.product.mapper;

import com.ecommerce.product.entity.ProductImage;
import com.ecommerce.product.dto.ProductImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductImageMapper {

    ProductImageDTO toDTO(ProductImage entity);

    ProductImage toEntity(ProductImageDTO dto);
}
