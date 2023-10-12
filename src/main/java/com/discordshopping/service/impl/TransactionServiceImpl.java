package com.discordshopping.service.impl;

import com.discordshopping.entity.Transaction;
import com.discordshopping.service.TransactionService;
import com.discordshopping.service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public Optional<Transaction> findById(String id) {
        return transactionRepository.findById(UUID.fromString(id));
    }
}
