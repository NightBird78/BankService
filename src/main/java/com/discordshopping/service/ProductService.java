package com.discordshopping.service;

import com.discordshopping.entity.Product;

import java.util.Optional;

public interface ProductService {
    Optional<Product> findById(String id);
}
