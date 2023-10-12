package com.discordshopping.service;


import com.discordshopping.entity.Transaction;

import java.util.Optional;

public interface TransactionService {
    Optional<Transaction> findById(String id);

}
