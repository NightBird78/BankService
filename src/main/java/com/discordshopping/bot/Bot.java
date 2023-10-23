package com.discordshopping.bot;

import com.discordshopping.bot.util.Constant;
import com.discordshopping.bot.util.Cookie;
import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.User;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.enums.AccountStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.InvalidEmailException;
import com.discordshopping.exception.InvalidIDBAException;
import com.discordshopping.mapper.AccountMapper;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.CurrencyService;
import com.discordshopping.service.UserService;
import com.discordshopping.util.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class Bot extends ListenerAdapter {

    private final UserService userService;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    private final AccountMapper accountMapper;
    private final Map<Long, Cookie> cookie = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot(
            UserService userService,
            AccountService accountService,
            CurrencyService currencyService,
            AccountMapper accountMapper) {
        this.userService = userService;
        this.accountService = accountService;
        this.currencyService = currencyService;
        this.accountMapper = accountMapper;
    }

    public static void bot(
            UserService userService,
            AccountService accountService,
            CurrencyService currencyService,
            AccountMapper accountMapper) throws InterruptedException {
        JDA jda = JDABuilder.createLight(Constant.token)
                .addEventListeners(new Bot(userService, accountService, currencyService, accountMapper))
                .build();

        jda.updateCommands().addCommands(
                        Commands.slash("ping", "Calculate ping of the bot"),
                        Commands.slash("register", "Register to miniBank system")
                                .addOption(OptionType.STRING, "firstname", "Yours first name")
                                .addOption(OptionType.STRING, "lastname", "Yours last name")
                                .addOption(OptionType.STRING, "email", "Yours e-mail, You can use non discord e-mail")
                                .addOption(OptionType.STRING, "password", "Don`t forget password!")
                                .addOption(OptionType.STRING, "earning", "Salary per year")
                                .addOption(OptionType.STRING, "address", "Yours living address")
                                .addOption(OptionType.STRING, "taxcode", "Yours tax code"),
                        Commands.slash("currency", "Change currency"),
                        Commands.slash("login", "Log-in bank account")
                                .addOption(OptionType.STRING, "email", "e-mail")
                                .addOption(OptionType.STRING, "password", "Did you remember?"),
                        Commands.slash("logout", "Log-out from account"),
                        Commands.slash("transfer", "Transfer money from you to someone")
                                .addOption(OptionType.STRING, "currency", "Currency of transfer amount")
                                .addOption(OptionType.STRING, "amount", "a lot of money")
                                .addOption(OptionType.STRING, "idba", "Who will get money by IDBA"),
                        Commands.slash("account", "Check yours account")
                )
                .queue();

        jda.awaitReady();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        logger.info("Loading currency from bank API...");
        long millis = System.currentTimeMillis();

        Map<CurrencyCode, Double> map;

        try {
            map = JsonParser.parseCurrency();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for (CurrencyCode cc : CurrencyCode.values()) {
            Double price;
            if (cc.toString().equals("PLN")) {
                price = 1d;
            } else {
                price = map.get(cc);
                if (price == null) {
                    LoggerFactory.getLogger(JsonParser.class).warn(String.format("\"%s\" not found", cc));
                    continue;
                }
            }
            Currency currency = new Currency(cc, price);

            currencyService.update(currency);
        }
        logger.info(String.format("Currency data loaded in %d milliseconds", System.currentTimeMillis() - millis));
        logger.info("API is ready!");
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:currency")) {
            if (cookie.containsKey(event.getUser().getIdLong())) {
                if (cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                    event.getMessage().delete().queue();

                    UserAccount account;

                    try {
                        account = accountService.findByEmail(cookie.get(event.getUser().getIdLong()).getEmail());
                    } catch (InvalidEmailException e) {
                        event.reply("something wrong").queue();
                        return;
                    }

                    account.setCurrencyCode(CurrencyCode.valueOf(event.getValues().get(0)));

                    accountService.save(account);

                    event.reply("success").queue();
                    return;
                }
            }
            event.reply("You don`t log in\nType **/login**").queue();
        }
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

                if (!MiniUtil.isValidEmail(email)) {
                    event.reply("invalid email").queue();
                    return;
                }

                User user = new User();

                user.setId(userId);
                user.setTaxCode(tax_code);
                user.setNickName(userName);
                user.setFirstName(firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase());
                user.setLastName(lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase());
                user.setEarning(earning);
                user.setEmail(email);
                user.setPassword(password);
                user.setAddress(address.toLowerCase());

                UserAccount userAccount = new UserAccount();

                userAccount.setId(UUID.randomUUID());
                userAccount.setUser(user);
                userAccount.setBalance(0d);
                userAccount.setAccountStatus(AccountStatus.Active);

                if (!userService.create(user)) {
                    event.reply("You already registered").queue();
                    return;
                }

                accountService.create(userAccount);
                logger.info(userName + " was success registered");

                event.reply("success to register").queue();
                cookie.put(event.getUser().getIdLong(), new Cookie("in", user.getEmail()));

            }
            case "currency" -> {
                try {
                    if (!cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                        event.reply("You don`t log in\nType **/login**").queue();
                        return;
                    }
                } catch (Exception e) {
                    event.reply("You don`t log in\nType **/login**").queue();
                    return;
                }

                StringSelectMenu.Builder builder = StringSelectMenu.create("menu:currency");
                for (String cur : Arrays.stream(CurrencyCode.values()).map(CurrencyCode::toString).sorted().toList()) {
                    builder.addOption(cur, cur);
                }
                StringSelectMenu menu = builder.build();

                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Currency manager");
                eb.setColor(Color.ORANGE);
                eb.setDescription("One all of this\nChoose your currency:");
                event.replyEmbeds(eb.build()).addActionRow(menu).queue();
            }
            case "login" -> {
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
                    cookie.put(event.getUser().getIdLong(), new Cookie("in", email));
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
            case "transfer" -> {

                if (cookie.containsKey(event.getUser().getIdLong())) {
                    if (cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {

                        double amount;
                        String idba;
                        String currency;

                        try {
                            amount = Objects.requireNonNull(event.getOption("amount")).getAsDouble();
                            idba = Objects.requireNonNull(event.getOption("idba")).getAsString();
                            currency = Objects.requireNonNull(event.getOption("currency")).getAsString();
                        } catch (Exception e) {
                            event.reply("Some field is empty").queue();
                            return;
                        }
                        UserAccount accountFrom;
                        UserAccount accountTo;

                        try {
                            accountFrom = accountService.findByEmail(cookie.get(event.getUser().getIdLong()).getEmail());
                            accountTo = accountService.findByIDBA(idba);
                        } catch (InvalidEmailException e) {
                            event.reply("some error").queue();
                            return;
                        } catch (InvalidIDBAException e) {
                            event.reply("invalid IDBA").queue();
                            return;
                        }

                        if (accountFrom.getIdba().equals(accountTo.getIdba())) {
                            event.reply("U can`t send money for yourself").queue();
                            return;
                        }

                        if (!accountService.transfer(accountFrom, accountTo, currency, amount)) {
                            event.reply("Not enough money").queue();
                            return;
                        }

                        event.reply("Check accounts").queue();
                    }
                }
                event.reply("You don`t log in\nType **/login**").queue();
            }
            case "account" -> {
                if (cookie.containsKey(event.getUser().getIdLong())) {
                    if (cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                        UserAccount account;

                        try {
                            account = accountService.findByEmail(cookie.get(event.getUser().getIdLong()).getEmail());
                        } catch (InvalidEmailException e) {
                            event.reply("something wrong").queue();
                            return;
                        }

                        EmbedBuilder eb = new EmbedBuilder();

                        eb.setTitle("Account manager");
                        eb.setColor(Color.GREEN);
                        eb.setDescription("information about your account");
                        eb.addField("IDBA", account.getIdba(), false);
                        eb.addField("Currency", account.getCurrencyCode() == null ? "/currency" : account.getCurrencyCode().toString(), false);
                        eb.addField("Balance", account.getBalance().toString(), false);
                        eb.addField("Status", account.getAccountStatus().toString(), false);

                        eb.setFooter(account.getCreatedAt().toString());

                        event.replyEmbeds(eb.build()).queue();

                        return;
                    }
                }
                event.reply("You don`t log in\nType **/login**").queue();
            }
        }
    }
}
