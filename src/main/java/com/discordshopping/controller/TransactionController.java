package com.discordshopping.controller;

import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/get/{id}")
    public TransactionDto getTransaction(@PathVariable("id") String id) {
        return transactionService.findDtoById(id);
    }
}