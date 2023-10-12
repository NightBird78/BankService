package com.discordshopping.mapper;

import com.discordshopping.entity.Product;
import com.discordshopping.entity.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productType", target = "type")
    @Mapping(source = "productStatus", target = "status")
    @Mapping(source = "createdAt", target = "date")
    ProductDto productToDto(Product product);
}
