package com.discordshopping.service;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;

import java.util.Optional;

public interface AccountService {

    Optional<UserAccount> findById(String id);

    Optional<UserAccount> findByIDBA(String idba);

    Optional<UserAccount> findByUserId(String id);

    boolean create(UserAccount account);

    boolean create(AccountDto accountDto);

    void save(UserAccount account);

    Optional<UserAccount> findByEmail(String email);

    boolean transfer(UserAccount from, UserAccount to, String currency, Double amount);
}
