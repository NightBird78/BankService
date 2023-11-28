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

import java.io.FileNotFoundException;
import java.util.Map;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class PreInitializer implements CommandLineRunner {

    private static final String config = "config.json";


    private final UserService userService;
    private final AccountService accountService;
    private final CurrencyService currencyService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String botToken;
    private String bankAPI;

    @Override
    public void run(String... args) {
        init();

        loadCurrency();
        loadDiscordAPI();
    }

    private void init() {
        Map<String, String> map;

        try {
            map = JsonParser.parseConfig(config);
        } catch (FileNotFoundException e) {
            logger.error("config file is not found!");
            return;
        }

        if (map == null) {
            logger.error("config file not readable");
            return;
        }

        botToken = map.get("botToken");
        bankAPI = map.get("bankAPI");
    }

    private void loadCurrency() {
        if (bankAPI == null) {
            return;
        }

        logger.info("Loading currency from bank API...");
        long millis = System.currentTimeMillis();

        Map<CurrencyCode, Double> map;

        try {
            map = JsonParser.parseCurrency(bankAPI);
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
        if (botToken == null) {
            return;
        }

        try {
            Bot.bot(userService, accountService, botToken);
        } catch (Exception e) {
            LoggerFactory.getLogger(PreInitializer.class).warn("Cannot startup Discord-API");
        }
    }
}