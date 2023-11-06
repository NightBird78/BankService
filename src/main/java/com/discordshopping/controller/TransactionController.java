package com.discordshopping.controller;

import com.discordshopping.bot.util.validator.annotation.ValidUUID;
import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/details")
    public TransactionDto getTransaction(@ValidUUID @Param("id") String id) {
        return transactionService.findDtoById(id);
    }
    @GetMapping("/get/all-by-account")
    public List<TransactionDto> getAllTransactions(@ValidUUID @Param("id") String id) {
        return transactionService.findAllByAccountId(id);
    }
}