package com.discordshopping.service.impl;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.InvalidCurrencyException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public Currency findByName(String name) {
        try {
            return currencyRepository.findById(CurrencyCode.valueOf(name.toUpperCase()))
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException(String.format("%s: \"%s\"", ErrorMessage.INVALID_CURRENCY, name));
        }
    }

    @Override
    public Currency findByName(CurrencyCode currencyCode) {
        return currencyRepository.findById(currencyCode)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
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

    @Override
    public List<CurrencyCode> findAll() {
        return currencyRepository.findAll().stream().map(Currency::getCurrencyCode).toList();
    }
}
