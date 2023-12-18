package com.discordshopping.service.impl;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.Transaction;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.TransactionMapper;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.TransactionService;
import com.discordshopping.service.repository.AccountRepository;
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
    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;

    @Override
    public Transaction findById(String id) {
        return transactionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public TransactionDto findDtoById(String id, String idba) {
        return transactionMapper.transactionToDto(findById(id), idba);
    }

    @Override
    public List<TransactionDto> findAllByAccountId(String id) {

        List<Transaction> allByAccountId = transactionRepository.findAllByAccountId(UUID.fromString(id));

        String idba = accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND)).getIdba();

        return allByAccountId.stream()
                .map((t) -> transactionMapper.transactionToDto(t, idba))
                .toList();
    }

    @Override
    public List<TransactionDtoShort> findAllByIdba(String idba) {

        List<Transaction> all = transactionRepository.findAllByIdba(idba);

        return all.stream()
                .map((t) -> transactionMapper.transactionToShortDto(t, idba))
                .toList();
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDtoShort> findAllShortByAccountId(String id) {

        List<Transaction> allByAccountId = transactionRepository.findAllByAccountId(UUID.fromString(id));

        String idba = accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND)).getIdba();

        System.out.println(idba);

        System.out.println(allByAccountId);

        return allByAccountId.stream()
                .map((t) -> transactionMapper.transactionToShortDto(t, idba))
                .toList();
    }

    @Override
    public Double checkTransact(String cFrom, String cTo, String amount) {
        Currency currencyF = currencyService.findByName(cFrom);
        Currency currencyT = currencyService.findByName(cTo);

        return (currencyF.price / currencyT.price) * Double.parseDouble(amount);
    }

    @Override
    public Double checkTransact(CurrencyCode cFrom, CurrencyCode cTo, Double amount) {
        Currency currencyF = currencyService.findByName(cFrom);
        Currency currencyT = currencyService.findByName(cTo);

        return (currencyF.price / currencyT.price) * amount;
    }


}
