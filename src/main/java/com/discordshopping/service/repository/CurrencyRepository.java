package com.discordshopping.service.repository;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, CurrencyCode> {
}
