package com.discordshopping.controller;

import com.discordshopping.bot.util.validator.annotation.ValidUUID;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;
import com.discordshopping.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/get/by-user")
    public AccountDto getAccount(@ValidUUID @Param("id") String id) {
        return accountService.findDtoByUserId(id);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public AccountDto update(@ValidUUID @Param("id") String id, @RequestBody AccountUpdatedDto accountUpdatedDto) {
        return accountService.merge(accountUpdatedDto, id);
    }
}