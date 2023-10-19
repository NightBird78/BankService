package com.discordshopping.service;

import com.discordshopping.entity.Product;
import com.discordshopping.entity.dto.ProductDto;

public interface ProductService {
    Product findById(String id);

    ProductDto findDtoById(String id);
}
