package com.discordshopping.service.impl;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.repository.AccountRepository;
import com.discordshopping.service.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public Optional<UserAccount> findById(String id) {
        return accountRepository.findById(UUID.fromString(id));
    }

    @Override
    public Optional<UserAccount> findByIDBA(String idba) {
        return accountRepository.findByIDBA(idba);
    }

    @Override
    public Optional<UserAccount> findByUserId(String id) {
        return accountRepository.findByUserId(UUID.fromString(id));
    }

    @Override
    @Transactional
    public boolean create(UserAccount account) {
        if (accountRepository.findById(account.getId()).isPresent()) {
            return false;
        }
        accountRepository.save(account);
        return true;
    }

    @Override
    public boolean create(AccountDto accountDto) {
        return false;
    }

    @Override
    public void save(UserAccount account) {
        accountRepository.save(account);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public boolean transfer(UserAccount from, UserAccount to, String currency, Double amount) {

        CurrencyCode fromC = from.getCurrencyCode();
        CurrencyCode toC = to.getCurrencyCode();

        Optional<Currency> optCf = currencyRepository.findById(fromC);
        Optional<Currency> optCt = currencyRepository.findById(toC);
        Optional<Currency> ofC = currencyRepository.findById(CurrencyCode.valueOf(currency));


        if (optCf.isEmpty() || optCt.isEmpty() || ofC.isEmpty()) {
            return false;
        }

        Double fromK = optCf.get().price;
        Double toK = optCt.get().price;
        Double ofK = ofC.get().price;

        Double amountT = amount * (ofK/toK);
        Double amountF = amount * (ofK/fromK);

        if (from.getBalance() < amountF){
            return false;
        }

        from.setBalance(from.getBalance() - amountF);
        to.setBalance(to.getBalance() + amountT);

        accountRepository.save(from);
        accountRepository.save(to);

        return true;
    }


}
