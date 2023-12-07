package com.discordshopping.service;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;

import java.util.List;

public interface CurrencyService {

    /**
     * @param name its name of CurrencyCode<br>
     *             same as id
     */
    Currency findByName(String name);
    Currency findByName(CurrencyCode currencyCode);

    /**
     * @param name its name of CurrencyCode<br>
     *             same as id
     */
    boolean existByName(CurrencyCode name);

    void create(Currency currency);

    void update(Currency currency);

    List<CurrencyCode> findAll();
}
