package com.discordshopping.bot;

import com.discordshopping.bot.util.Constant;
import com.discordshopping.entity.User;
import com.discordshopping.service.UserService;
import com.discordshopping.service.repository.UserRepository;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@Service
public class Bot extends ListenerAdapter {

    private final UserService userService;
    private final UserRepository userRepository;

    public Bot(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public static void bot(UserService userService, UserRepository userRepository) throws InterruptedException {
        JDA jda = JDABuilder.createLight(Constant.token)
                .addEventListeners(new Bot(userService, userRepository))
                .build();

        jda.updateCommands().addCommands(
                        Commands.slash("ping", "Calculate ping of the bot"),
                        Commands.slash("register", "register to database")
                                .addOption(OptionType.STRING, "firstname", "yours first name for database")
                                .addOption(OptionType.STRING, "lastname", "yours last name for database")
                                .addOption(OptionType.STRING, "email", "yours email, oyu can use non discord email")
                )
                .queue();

        jda.awaitReady();
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
                Long userId = Long.parseLong(event.getUser().getId());
                String userName = event.getUser().getName();
                String firstName = Objects.requireNonNull(event.getOption("firstname")).getAsString();
                String lastName = Objects.requireNonNull(event.getOption("lastname")).getAsString();
                String email = Objects.requireNonNull(event.getOption("email")).getAsString();

                Pattern pattern = Pattern.compile("[a-zA-Z0-9_]+@[a-z]+\\.[a-z]+");
                if (!pattern.matcher(email).find()){
                    event.reply("invalid email").queue();
                    return;
                }
                User user = new User(userId, userName, firstName, lastName, email);

                System.out.println(user);

                userService.create(user);


                event.reply("success to register").queue();
            }
        }
    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("API is ready!");
    }

}
