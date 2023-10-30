package com.discordshopping.controller;

import com.discordshopping.bot.util.ValidUUID;
import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/{id}")
    public TransactionDto getTransaction(@ValidUUID @PathVariable("id") String id) {
        return transactionService.findDtoById(id);
    }
}