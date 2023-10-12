package com.discordshopping.service.impl;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Optional<UserAccount> findById(String id) {
        return accountRepository.findById(UUID.fromString(id));
    }

    @Override
    @Transactional
    public boolean create(UserAccount account) {
        if (accountRepository.findById(account.getId()).isPresent()){
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean create(AccountDto accountDto) {
        return false;
    }


}
