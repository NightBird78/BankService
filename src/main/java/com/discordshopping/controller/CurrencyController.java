package com.discordshopping.controller;

import com.discordshopping.entity.Currency;
import com.discordshopping.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/get")
    public Currency getCurrency(@Param("name") String name) {
        return currencyService.findByName(name);
    }
}
