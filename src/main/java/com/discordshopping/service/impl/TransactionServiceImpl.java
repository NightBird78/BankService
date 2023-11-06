package com.discordshopping.service.impl;

import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.TransactionMapper;
import com.discordshopping.service.TransactionService;
import com.discordshopping.service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Transaction findById(String id) {
        return transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public TransactionDto findDtoById(String id) {
        return transactionMapper.transactionToDto(findById(id));
    }

    @Override
    public List<TransactionDto> findAllByAccountId(String id) {
        return transactionMapper.transactionToDto(transactionRepository.findAllByAccountId(id));
    }
}
