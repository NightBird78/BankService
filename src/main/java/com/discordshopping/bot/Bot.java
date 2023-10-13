package com.discordshopping.bot;

import com.discordshopping.bot.util.Constant;
import com.discordshopping.bot.util.Cookie;
import com.discordshopping.util.JsonParser;
import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.User;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.enums.AccountStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.UserService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Bot extends ListenerAdapter {

    private final UserService userService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final Map<Long, Cookie> cookie = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot(UserService userService, AccountService accountService, CurrencyService currencyService) {
        this.userService = userService;
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    public static void bot(UserService userService, AccountService accountService, CurrencyService currencyService) throws InterruptedException {
        JDA jda = JDABuilder.createLight(Constant.token)
                .addEventListeners(new Bot(userService, accountService, currencyService))
                .build();

        jda.updateCommands().addCommands(
                        Commands.slash("ping", "Calculate ping of the bot"),
                        Commands.slash("register", "register to database")
                                .addOption(OptionType.STRING, "firstname", "yours first name for database")
                                .addOption(OptionType.STRING, "lastname", "yours last name for database")
                                .addOption(OptionType.STRING, "email", "yours email, oyu can use non discord email")
                                .addOption(OptionType.STRING, "password", "don`t forget password")
                                .addOption(OptionType.STRING, "earning", "salary per year")
                                .addOption(OptionType.STRING, "address", "yours live address")
                                .addOption(OptionType.STRING, "taxcode", "yours tax code"),
                        Commands.slash("account", "create or manage your payment account"),
                        Commands.slash("login", "log in bank account")
                                .addOption(OptionType.STRING, "email", "email")
                                .addOption(OptionType.STRING, "password", "password"),
                        Commands.slash("logout", "log out from account")
                )
                .queue();

        jda.awaitReady();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        logger.info("Loading currency from API...");
        long millis = System.currentTimeMillis();

        Map<String, Double> map = JsonParser.parseCurrencyToMap();

        for (CurrencyCode cc : CurrencyCode.values()) {
            Double price;
            if (cc.toString().equals("PLN")) {
                price = 1d;
            } else {
                price = map.get(cc.toString());
            }
            Currency currency = new Currency(cc, price);

            currencyService.update(currency);
        }

        logger.info(String.format("Currency data loaded in %d milliseconds", System.currentTimeMillis() - millis));

        logger.info("API is ready!");
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
                UUID userId;
                String tax_code;
                String userName;
                String firstName;
                String lastName;
                String email;
                String address;
                double earning;
                String password;
                try {
                    userId = MiniUtil.encode(event.getUser().getId());
                    tax_code = Objects.requireNonNull(event.getOption("taxcode")).getAsString();
                    userName = event.getUser().getName();
                    firstName = Objects.requireNonNull(event.getOption("firstname")).getAsString();
                    lastName = Objects.requireNonNull(event.getOption("lastname")).getAsString();
                    email = Objects.requireNonNull(event.getOption("email")).getAsString();
                    address = Objects.requireNonNull(event.getOption("address")).getAsString();
                    earning = Double.parseDouble(Objects.requireNonNull(event.getOption("earning")).getAsString());
                    password = MiniUtil.encode(
                                    Objects.requireNonNull(event.getOption("password"))
                                            .getAsString()
                            )
                            .toString();
                } catch (NumberFormatException e) {
                    event.reply("Something is wrong, I think there was a mistake with the number.").queue();
                    return;
                } catch (Exception e) {
                    event.reply("Some field is empty!").queue();
                    return;
                }

                Pattern pattern = Pattern.compile("[a-z0-9_\\.]+@[a-z]+\\.[a-z]+");

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
                        firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase(),
                        lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase(),
                        earning,
                        email,
                        password,
                        address.toLowerCase(),
                        LocalDateTime.now(),
                        LocalDateTime.now());

                UserAccount userAccount = new UserAccount();

                userAccount.setId(UUID.randomUUID());
                userAccount.setBalance(0d);
                userAccount.setUser(user);
                userAccount.setCreatedAt(LocalDateTime.now());
                userAccount.setUpdatedAt(LocalDateTime.now());
                userAccount.setAccountStatus(AccountStatus.Active);

                if (!userService.create(user)) {
                    event.reply("You already registered").queue();
                    return;
                }

                accountService.create(userAccount);
                logger.info(userName + " was success registered");

                event.reply("success to register").queue();
                cookie.put(event.getUser().getIdLong(), new Cookie("in"));

            }
            case "account" -> {
                try {
                    if (!cookie.get(event.getUser().getIdLong()).getLogin().equals("in")){
                        event.reply("You don`t log in\nType **/login**").queue();
                        return;
                    }
                } catch (Exception e) {
                    event.reply("You don`t log in\nType **/login**").queue();
                    return;
                }

                StringSelectMenu.Builder builder = StringSelectMenu.create("menu:id");
                for (String cur : Arrays.stream(CurrencyCode.values()).map(CurrencyCode::toString).sorted().toList()) {
                    builder.addOption(cur, cur);
                }
                StringSelectMenu menu = builder.build();

                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("account manager");
                eb.setColor(Color.ORANGE);
                eb.setDescription("choose all of this");

                EmbedBuilder eb2 = new EmbedBuilder();

                eb2.setTitle("account manager");
                eb2.setColor(Color.CYAN);
                eb2.setDescription("choose your currency");

                event.replyEmbeds(eb.build()).addEmbeds(eb2.build()).addActionRow(menu).queue();
            }
            case "login" -> {

                try {
                    if (!userService.existById(MiniUtil.encode(event.getUser().getId()))) {
                        event.reply("You don`t registered\nType **/register**").queue();
                        return;
                    }
                } catch (NoSuchAlgorithmException ignored) {
                }
                String email;
                String password;
                try {
                    email = Objects.requireNonNull(event.getOption("email")).getAsString();
                    password = MiniUtil.encode(
                                    Objects.requireNonNull(event.getOption("password"))
                                            .getAsString()
                            )
                            .toString();
                } catch (Exception e) {
                    event.reply("some field empty").queue();
                    return;
                }

                try {
                    if (cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                        event.reply("You already logged in").queue();
                        return;
                    }
                } catch (NullPointerException ignored) {
                }

                if (!userService.existByEmail(email)) {
                    event.reply("user with this email does not exist").queue();
                    return;
                }

                if (userService.existByPasswordAndEmail(password, email)) {
                    event.reply("You success log in").queue();
                    cookie.put(event.getUser().getIdLong(), new Cookie("in"));
                    logger.info(event.getUser().getName() + " logged in");
                    return;
                }
                event.reply("wrong email or password").queue();
            }
            case "logout" -> {
                try {
                    if (cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                        cookie.remove(event.getUser().getIdLong());
                        logger.info(event.getUser().getName() + " logged out");
                        event.reply("You success logged out").queue();
                    }
                } catch (NullPointerException ignored) {
                    event.reply("You don`t logged in").queue();
                }
            }
        }
    }
}
