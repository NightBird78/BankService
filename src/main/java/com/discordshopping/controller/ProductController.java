package com.discordshopping.controller;

import com.discordshopping.bot.util.ValidUUID;
import com.discordshopping.entity.dto.ProductDto;
import com.discordshopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get/{id}")
    public ProductDto getProduct(@ValidUUID @PathVariable("id") String id) {
        return productService.findDtoById(id);
    }
}
