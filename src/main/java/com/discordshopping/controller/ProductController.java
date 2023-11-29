package com.discordshopping.controller;

import com.discordshopping.validation.annotation.ValidUUID;
import com.discordshopping.dto.ProductDto;
import com.discordshopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get/detail")
    public ProductDto getProduct(@ValidUUID @Param("id") String id) {
        return productService.findDtoById(id);
    }

    @GetMapping("/get/all")
    public List<ProductDto> getAllProduct() {
        return productService.findAll();
    }
}
