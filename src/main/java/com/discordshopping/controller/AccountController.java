package com.discordshopping.controller;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AccountMapper;
import com.discordshopping.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/get/{id}")
    public AccountDto getAccount(@PathVariable("id") String id) {
        if (!MiniUtil.isValidUUID(id)){
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }

        Optional<UserAccount> opt = accountService.findById(id);

        if (opt.isPresent()){
            return accountMapper.accountToDto(opt.get());
        }
        throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public AccountDto update(@PathVariable("id") String id, @RequestBody AccountUpdatedDto accountUpdatedDto) {
        if (!MiniUtil.isValidUUID(id)){
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }

        Optional<UserAccount> opt = accountService.findById(id);

        if (opt.isEmpty()){
            throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
        }

        UserAccount account = accountMapper.merge(accountMapper.dtoToAccount(accountUpdatedDto), opt.get());

        accountService.save(account);

        return accountMapper.accountToDto(account);
    }
}