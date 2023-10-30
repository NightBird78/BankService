package com.discordshopping.service.impl;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.InvalidEmailException;
import com.discordshopping.exception.InvalidIDBAException;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AccountMapper;
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
    private final AccountMapper accountMapper;

    @Override
    public UserAccount findById(String id) {
        return accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public UserAccount findByIDBA(String idba) {

        if (!MiniUtil.isValidIDBA(idba)) {
            throw new InvalidIDBAException(ErrorMessage.INVALID_IDBA);
        }

        return accountRepository.findByIDBA(idba)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));

    }

    @Override
    public UserAccount findByUserId(String id) {
        if (!MiniUtil.isValidUUID(id)) {
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }
        return accountRepository.findByUserId(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
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
    public UserAccount findByEmail(String email) {

        if (!MiniUtil.isValidEmail(email)) {
            throw new InvalidEmailException(ErrorMessage.INVALID_EMAIL_FORMAT);
        }

        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
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

        Double amountT = amount * (ofK / toK);
        Double amountF = amount * (ofK / fromK);

        if (from.getBalance() < amountF) {
            return false;
        }

        from.setBalance(from.getBalance() - amountF);
        to.setBalance(to.getBalance() + amountT);

        accountRepository.save(from);
        accountRepository.save(to);

        return true;
    }

    @Override
    public AccountDto findDtoById(String id) {
        return accountMapper.accountToDto(findById(id));
    }

    @Override
    public AccountDto merge(AccountUpdatedDto dto, String id) {

        UserAccount account = accountMapper.merge(
                accountMapper.dtoToAccount(dto), findById(id));

        save(account);

        return accountMapper.accountToDto(account);
    }
}