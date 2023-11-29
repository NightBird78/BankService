package com.discordshopping.service;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.dto.AccountDto;
import com.discordshopping.dto.AccountUpdatedDto;
import com.discordshopping.dto.TransactionDto;

public interface AccountService {

    UserAccount findById(String id);

    UserAccount findByIDBA(String idba);

    UserAccount findByUserId(String id);

    boolean create(UserAccount account);

    void save(UserAccount account);

    UserAccount findByEmail(String email);

    boolean transfer(UserAccount from, UserAccount to, String currency, Double amount, String describe);

    TransactionDto transfer(String fromIDBA, String toIDBA, String currency, String amount, String description, String type);

    AccountDto findDtoById(String id);

    AccountDto merge(AccountUpdatedDto dto, String id);

    AccountDto findDtoByUserId(String id);
}
