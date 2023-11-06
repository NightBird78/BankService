package com.discordshopping.components;

import com.discordshopping.bot.Bot;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.UserService;
import com.discordshopping.util.JsonParser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class PreInitializer implements CommandLineRunner {

    private final UserService userService;
    private final AccountService accountService;
    private final CurrencyService currencyService;

    @Override
    public void run(String... args) {
        loadCurrency();
        loadDiscordAPI();
    }

    private void loadCurrency() {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        logger.info("Loading currency from bank API...");
        long millis = System.currentTimeMillis();

        Map<CurrencyCode, Double> map;

        try {
            map = JsonParser.parseCurrency();
        } catch (Exception e) {
            logger.warn("cannot load currency from bank-API");
            return;
        }

        for (CurrencyCode cc : CurrencyCode.values()) {
            Double price;
            if (cc.toString().equals("PLN")) {
                price = 1d;
            } else {
                price = map.get(cc);
                if (price == null) {
                    LoggerFactory.getLogger(JsonParser.class).warn("'{}' not found", cc);

                    continue;
                }
            }
            Currency currency = new Currency(cc, price);

            currencyService.update(currency);
        }
        logger.info("Currency data loaded in {} milliseconds", System.currentTimeMillis() - millis);

    }

    private void loadDiscordAPI() {
        try {
            Bot.bot(userService, accountService);
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(PreInitializer.class).warn("Cannot load Discord-API");
            Thread.currentThread().interrupt();
        }
    }
}
