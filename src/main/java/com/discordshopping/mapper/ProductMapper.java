package com.discordshopping.mapper;

import com.discordshopping.entity.Product;
import com.discordshopping.dto.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "productType", target = "type")
    @Mapping(source = "productStatus", target = "status")
    @Mapping(source = "createdAt", target = "date")
    ProductDto productToDto(Product product);

    List<ProductDto> productToDto(List<Product> all);
}
