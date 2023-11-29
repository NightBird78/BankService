package com.discordshopping.service;


import com.discordshopping.entity.Transaction;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;

import java.util.List;

public interface TransactionService {
    Transaction findById(String id);

    TransactionDto findDtoById(String id, String idba);

    List<TransactionDto> findAllByAccountId(String id);

    Transaction save(Transaction transaction);

    List<TransactionDtoShort> findAllShortByAccountId(String id);
}
