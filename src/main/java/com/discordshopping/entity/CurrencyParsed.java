package com.discordshopping.entity;

import com.discordshopping.entity.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class CurrencyParsed implements Comparable<CurrencyParsed> {
    private LocalDateTime date;
    private CurrencyCode code;
    private Double price;

    @Override
    public int compareTo(@NotNull CurrencyParsed o) {
        return date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyParsed currency)) return false;
        return code == currency.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public Currency getCurrency() {
        return new Currency(code, price);
    }
}
