package com.discordshopping.service.impl;

import com.discordshopping.entity.Product;
import com.discordshopping.service.ProductService;
import com.discordshopping.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(UUID.fromString(id));
    }
}
