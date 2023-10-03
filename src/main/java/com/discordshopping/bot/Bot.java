package com.discordshopping.bot;

import com.discordshopping.bot.util.Constant;
import com.discordshopping.entity.User;
import com.discordshopping.service.UserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Bot extends ListenerAdapter {

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot(UserService userService) {
        this.userService = userService;
    }

    public static void bot(UserService userService) throws InterruptedException {
        JDA jda = JDABuilder.createLight(Constant.token)
                .addEventListeners(new Bot(userService))
                .build();

        jda.updateCommands().addCommands(
                        Commands.slash("ping", "Calculate ping of the bot"),
                        Commands.slash("register", "register to database")
                                .addOption(OptionType.STRING, "taxcode", "yours tax code")
                                .addOption(OptionType.STRING, "firstname", "yours first name for database")
                                .addOption(OptionType.STRING, "lastname", "yours last name for database")
                                .addOption(OptionType.STRING, "email", "yours email, oyu can use non discord email")
                                .addOption(OptionType.STRING, "address", "yours live address")
                )
                .queue();

        jda.awaitReady();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("API is ready!");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();
                event.reply("Pong!").flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms",
                                        System.currentTimeMillis() - time)
                        )
                        .queue();
            }
            case "register" -> {
                long userId;
                String tax_code;
                String userName;
                String firstName;
                String lastName;
                String email;
                String address;
                try {
                    userId = Long.parseLong(event.getUser().getId());
                    tax_code = Objects.requireNonNull(event.getOption("taxcode")).getAsString();
                    userName = event.getUser().getName();
                    firstName = Objects.requireNonNull(event.getOption("firstname")).getAsString();
                    lastName = Objects.requireNonNull(event.getOption("lastname")).getAsString();
                    email = Objects.requireNonNull(event.getOption("email")).getAsString();
                    address = Objects.requireNonNull(event.getOption("address")).getAsString();
                } catch (Exception e){
                    event.reply("Some field is empty!").queue();
                    return;
                }

                Pattern pattern = Pattern.compile("[a-zA-Z0-9_\\.]+@[a-z]+\\.[a-z]+");

                Matcher matcher = pattern.matcher(email);
                if (matcher.find()) {
                    if (!email.equals(matcher.group())) {
                        event.reply("invalid email").queue();
                        return;
                    }
                } else {
                    event.reply("invalid email").queue();
                    return;
                }
                User user = new User(
                        userId,
                        tax_code,
                        userName,
                        firstName,
                        lastName,
                        email,
                        address,
                        LocalDateTime.now());

                logger.info(userName + " was success registered");

                if (!userService.create(user)){
                    event.reply("You already registered").queue();
                    return;
                }

                event.reply("success to register").queue();
            }
        }
    }
}
