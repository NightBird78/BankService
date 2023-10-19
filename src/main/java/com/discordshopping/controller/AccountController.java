package com.discordshopping.controller;

import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;
import com.discordshopping.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/get/{id}")
    public AccountDto getAccount(@PathVariable("id") String id) {
        return accountService.findDtoById(id);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public AccountDto update(@PathVariable("id") String id, @RequestBody AccountUpdatedDto accountUpdatedDto) {
        return accountService.merge(accountUpdatedDto, id);
    }
}