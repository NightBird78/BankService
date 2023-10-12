package com.discordshopping.controller;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.mapper.AccountMapper;
import com.discordshopping.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/get/{id}")
    public AccountDto getAccount(@PathVariable("id") String id){
        Optional<UserAccount> opt = accountService.findById(id);

        if (opt.isPresent()){
            return accountMapper.accountToDto(opt.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}