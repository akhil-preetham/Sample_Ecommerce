package com.ecommerce.product.mapper;

import com.ecommerce.product.entity.Product;
import com.ecommerce.product.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductMapper {

    ProductDTO toDTO(Product entity);

    Product toEntity(ProductDTO dto);
}
