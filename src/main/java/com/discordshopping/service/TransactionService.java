package com.discordshopping.service;


import com.discordshopping.entity.Transaction;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
import com.discordshopping.entity.enums.CurrencyCode;

import java.util.List;

public interface TransactionService {
    Transaction findById(String id);

    TransactionDto findDtoById(String id, String idba);

    List<TransactionDto> findAllByAccountId(String id);

    Transaction save(Transaction transaction);

    List<TransactionDtoShort> findAllShortByAccountId(String id);

    Double checkTransact(String cFrom, String cTo, String amount);

    Double checkTransact(CurrencyCode cFrom, CurrencyCode cTo, Double amount);
}
