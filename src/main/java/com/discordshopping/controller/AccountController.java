package com.discordshopping.controller;

import com.discordshopping.validation.annotation.ValidIDBA;
import com.discordshopping.validation.annotation.ValidUUID;
import com.discordshopping.dto.AccountDto;
import com.discordshopping.dto.AccountUpdatedDto;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "any response", description = "desc of response")
    @GetMapping("/get/by-user")
    public AccountDto getByUser(@Parameter(description = "Id of user")
            @ValidUUID @Param("id") String id) {
        return accountService.findDtoByUserId(id);
    }

    @GetMapping("/get")
    public AccountDto getAccount(@ValidUUID @Param("id") String id) {
        return accountService.findDtoById(id);
    }

    @PostMapping(value = "/update")
    public AccountDto update(@ValidUUID @Param("id") String id, @RequestBody AccountUpdatedDto accountUpdatedDto) {
        return accountService.merge(accountUpdatedDto, id);
    }

    @Transactional
    @PostMapping(value = "/transfer")
    public TransactionDto transfer(
            @ValidIDBA @Param("from") String from,
            @ValidIDBA @Param("to") String to,
            @Param("desc") String desc,
            @Param("code") String code,
            @Param("amount") String amount,
            @Param("type") String type) {
        return accountService.transfer(from, to, code, amount, desc, type);
    }
}