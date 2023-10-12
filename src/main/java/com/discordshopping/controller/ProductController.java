package com.discordshopping.controller;

import com.discordshopping.entity.Product;
import com.discordshopping.entity.dto.ProductDto;
import com.discordshopping.mapper.ProductMapper;
import com.discordshopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/get/{id}")
    public ProductDto getProduct(@PathVariable("id") String id){
        Optional<Product> opt = productService.findById(id);

        if (opt.isPresent()){
            return productMapper.productToDto(opt.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
