package com.discordshopping.service.impl;

import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.UserService;
import com.discordshopping.util.Util;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.dto.AccountDto;
import com.discordshopping.dto.AccountUpdatedDto;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.entity.enums.TransactionType;
import com.discordshopping.exception.InvalidEmailException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.AccountMapper;
import com.discordshopping.mapper.TransactionMapper;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.TransactionService;
import com.discordshopping.service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final UserService userService;

    @Override
    public UserAccount findById(String id) {
        return accountRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    public UserAccount findByIDBA(String idba) {
        return accountRepository.findByIDBA(idba)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));

    }

    @Override
    public UserAccount findByUserId(String id) {
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
    public void save(UserAccount account) {
        accountRepository.save(account);
    }

    @Override
    public UserAccount findByEmail(String email) {

        if (!Util.isValidEmail(email)) {
            throw new InvalidEmailException(ErrorMessage.INVALID_EMAIL_FORMAT);
        }

        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.DATA_NOT_FOUND));
    }

    @Override
    @Transactional
    public boolean transfer(UserAccount from, UserAccount to, String currency, Double amount, String describe) {
        try {
            transfer(from.getIdba(), to.getIdba(), currency, amount.toString(), describe, TransactionType.transferFunds.toString());
            return true;
        } catch (Throwable ignore) {
            return false;
        }
    }

    @Override
    @Transactional
    public TransactionDto transfer(String fromIDBA, String toIDBA, String currency, String amountSrt, String description, String type) {
        Optional<UserAccount> optFrom = accountRepository.findByIDBA(fromIDBA);
        Optional<UserAccount> optTo = accountRepository.findByIDBA(toIDBA);

        if (optFrom.isEmpty() || optTo.isEmpty()) {
            throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
        }

        try {
            CurrencyCode.valueOf(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException(ErrorMessage.INVALID_CURRENCY);
        }

        UserAccount from = optFrom.get();
        UserAccount to = optTo.get();

        CurrencyCode fromC = from.getCurrencyCode();
        CurrencyCode toC = to.getCurrencyCode();

        if (fromC == null) {
            throw new NotFoundException("currency of user with IDBA: %s, not setted".formatted(fromIDBA));
        }

        if (toC == null) {
            throw new NotFoundException("currency of user with IDBA: %s, not setted".formatted(toIDBA));
        }

        Currency ofC = currencyService.findByName(currency);

        double amount = Double.parseDouble(amountSrt);

        Double amountT = transactionService.checkTransact(ofC.getCurrencyCode(), toC, amount);
        Double amountF = transactionService.checkTransact(ofC.getCurrencyCode(), fromC, amount);

        if (from.getBalance() < amountF) {
            throw new IllegalArgumentException("not enough of money");
        }

        from.setBalance(from.getBalance() - amountF);
        to.setBalance(to.getBalance() + amountT);

        accountRepository.save(from);
        accountRepository.save(to);

        Transaction transaction = new Transaction();
        transaction.setAmountTo(amountT);
        transaction.setAmountFrom(amountF);
        transaction.setTransactionType(TransactionType.valueOf(type));
        transaction.setDescription(description);
        transaction.setDebitUserAccount(from);
        transaction.setCreditUserAccount(to);

        return transactionMapper.transactionToDto(transactionService.save(transaction), fromIDBA);
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

    @Override
    public AccountDto findDtoByUserId(String id) {
        return accountMapper.accountToDto(findByUserId(id));
    }

    @Override
    public List<AccountDto> findAllDtoByEmail(String email) {
        return accountMapper.accountToDto(
                accountRepository.findAllByUserId(
                        UUID.fromString(
                                userService.findDtoByEmail(email).getId()
                        )
                )
        );
    }
}