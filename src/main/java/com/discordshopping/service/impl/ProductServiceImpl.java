package com.discordshopping.service.impl;

import com.discordshopping.entity.Product;
import com.discordshopping.entity.dto.ProductDto;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.ProductMapper;
import com.discordshopping.service.ProductService;
import com.discordshopping.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Product findById(String id) {
        return productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public ProductDto findDtoById(String id) {
        return productMapper.productToDto(findById(id));
    }

    @Override
    public List<ProductDto> findAll() {
        return productMapper.productToDto(productRepository.findAll());
    }
}