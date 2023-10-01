package com.discordshopping.components;

import com.discordshopping.bot.Bot;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JDAInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        Bot.bot(userService);
    }
}
