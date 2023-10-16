package com.discordshopping.components;

import com.discordshopping.bot.Bot;
import com.discordshopping.mapper.AccountMapper;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JDAInitializer implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final AccountMapper accountMapper;

    @Override
    public void run(String... args) throws Exception {
        Bot.bot(userService, accountService, currencyService, accountMapper);
    }
}
