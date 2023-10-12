package com.discordshopping.service;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;

import java.util.Optional;

public interface AccountService {

    Optional<UserAccount> findById(String id);

    boolean create(UserAccount account);

    boolean create(AccountDto accountDto);
}
