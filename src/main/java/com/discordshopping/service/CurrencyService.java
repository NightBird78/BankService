package com.discordshopping.service;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;

public interface CurrencyService {

    /**
     * @param name its name of CurrencyCode<br>
     *             same as id
     */
    Currency findByName(String name);

    /**
     * @param name its name of CurrencyCode<br>
     *             same as id
     */
    boolean existByName(CurrencyCode name);

    void create(Currency currency);

    void update(Currency currency);
}
