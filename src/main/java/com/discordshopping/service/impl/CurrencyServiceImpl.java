package com.discordshopping.service.impl;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    @Override
    public Optional<Currency> findByName(CurrencyCode name) {
        return currencyRepository.findById(name);
    }

    @Override
    public boolean existByName(CurrencyCode name) {
        return currencyRepository.existsById(name);
    }

    @Override
    public void create(Currency currency) {
        currencyRepository.save(currency);
    }

    @Override
    public void update(Currency currency) {
        create(currency);
    }
}
