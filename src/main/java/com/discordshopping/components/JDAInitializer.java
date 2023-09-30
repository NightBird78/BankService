package com.discordshopping.components;

import com.discordshopping.bot.Bot;
import com.discordshopping.service.UserService;
import com.discordshopping.service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JDAInitializer implements CommandLineRunner {
    private final JDAEventListener jdaEventListener;
    private final UserService userService;
    private final UserRepository userRepository;

    // Constructor injection
    public JDAInitializer(JDAEventListener jdaEventListener, UserService userService, UserRepository userRepository) {
        this.jdaEventListener = jdaEventListener;
        this.userService = userService;
        this.userRepository = userRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Bot.bot(userService, userRepository);
    }
}
