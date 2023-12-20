package com.discordshopping.service;


import com.discordshopping.entity.Transaction;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
import com.discordshopping.entity.enums.CurrencyCode;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    Transaction findById(String id);

    TransactionDto findDtoById(String id, String idba);

    List<TransactionDto> findAllByAccountId(String id);
    List<TransactionDtoShort> findAllByIdba(String idba);

    Transaction save(Transaction transaction);

    List<TransactionDtoShort> findAllShortByAccountId(String id);

    BigDecimal checkTransact(String cFrom, String cTo, String amount);

    BigDecimal checkTransact(CurrencyCode cFrom, CurrencyCode cTo, Double amount);
}
