package com.discordshopping.controller;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/get/{name}")
    public Currency getCurrency(@PathVariable("name") String name){
        Optional<Currency> opt;
        try {
            opt = currencyService.findByName(CurrencyCode.valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(ErrorMessage.INVALID_CURRENCY);
        }

        if (opt.isPresent()){
            return  opt.get();
        }
        throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
    }
}
