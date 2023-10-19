package com.discordshopping.service;


import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.dto.TransactionDto;

public interface TransactionService {
    Transaction findById(String id);

    TransactionDto findDtoById(String id);
}
