package com.discordshopping.controller;

import com.discordshopping.validation.annotation.ValidIDBA;
import com.discordshopping.validation.annotation.ValidUUID;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
import com.discordshopping.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/detail")
    public TransactionDto getTransaction(@ValidUUID @Param("id") String id, @ValidIDBA @Param("idba") String idba) {
        return transactionService.findDtoById(id, idba);
    }

    @GetMapping("/get/all/by-account")
    public List<TransactionDto> getAllTransactions(@ValidUUID @Param("id") String id) {
        return transactionService.findAllByAccountId(id);
    }

    @GetMapping("/get/all/by-account/short")
    public List<TransactionDtoShort> getAllShortTransactionsByAccountId(@ValidUUID @Param("id") String id) {
        return transactionService.findAllShortByAccountId(id);
    }

    @GetMapping("/get/all/by-idba/short")
    public List<TransactionDtoShort> getAllShortTransactionsByIdba(@ValidIDBA @Param("idba") String idba) {
        return transactionService.findAllByIdba(idba);
    }

    @GetMapping("/api/currency-convert")
    public BigDecimal check(
            @Param("from") String from,
            @Param("to") String to,
            @Param("amount") String amount) {

        return transactionService.checkTransact(from, to, amount);
    }
}