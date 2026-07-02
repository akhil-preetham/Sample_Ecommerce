package com.ecommerce.product.mapper;

import com.ecommerce.product.entity.Category;
import com.ecommerce.product.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CategoryMapper {

    CategoryDTO toDTO(Category entity);

    Category toEntity(CategoryDTO dto);
}
