package com.discordshopping.service;

import com.discordshopping.entity.Product;
import com.discordshopping.dto.ProductDto;

import java.util.List;

public interface ProductService {
    Product findById(String id);

    ProductDto findDtoById(String id);

    List<ProductDto> findAll();
}
