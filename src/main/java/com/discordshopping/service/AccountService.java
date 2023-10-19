package com.discordshopping.service;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;

public interface AccountService {

    UserAccount findById(String id);

    UserAccount findByIDBA(String idba);

    UserAccount findByUserId(String id);

    boolean create(UserAccount account);

    boolean create(AccountDto accountDto);

    void save(UserAccount account);

    UserAccount findByEmail(String email);

    boolean transfer(UserAccount from, UserAccount to, String currency, Double amount);

    AccountDto findDtoById(String id);

    AccountDto merge(AccountUpdatedDto dto, String id);
}
