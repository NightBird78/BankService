package com.discordshopping.controller;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Product;
import com.discordshopping.entity.dto.ProductDto;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.ProductMapper;
import com.discordshopping.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/get/{id}")
    public ProductDto getProduct(@PathVariable("id") String id){

        if (!MiniUtil.isValidUUID(id)){
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }

        Optional<Product> opt = productService.findById(id);

        if (opt.isPresent()){
            return productMapper.productToDto(opt.get());
        }
        throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
    }
}
