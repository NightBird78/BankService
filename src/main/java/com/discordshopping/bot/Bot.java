package com.discordshopping.bot;

import com.discordshopping.util.Cookie;
import com.discordshopping.util.Util;
import com.discordshopping.entity.User;
import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.enums.AccountStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import com.discordshopping.exception.InvalidEmailException;
import com.discordshopping.exception.InvalidIDBAException;
import com.discordshopping.service.AccountService;
import com.discordshopping.service.UserService;
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

    String warn1 = "You don`t log in\nType **/login**";

    private final UserService userService;
    private final AccountService accountService;
    private final Map<Long, Cookie> cookie = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    public Bot(
            UserService userService,
            AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    public static void bot(
            UserService userService,
            AccountService accountService,
            String token) throws InterruptedException {
        JDA jda = JDABuilder.createLight(token)
                .addEventListeners(new Bot(userService, accountService))
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
        logger.info("Discord-API is ready!");
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:currency")) {
            if (cookie.containsKey(event.getUser().getIdLong()) && (cookie.get(event.getUser().getIdLong()).getLogin().equals("in"))) {
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
            event.reply(warn1).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String name = event.getName();
        switch (name) {
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
                String taxCode;
                String userName;
                String firstName;
                String lastName;
                String email;
                String address;
                double earning;
                String password;
                try {
                    userId = Util.encode(event.getUser().getId());
                    taxCode = Objects.requireNonNull(event.getOption("taxcode")).getAsString();
                    userName = event.getUser().getName();
                    firstName = Objects.requireNonNull(event.getOption("firstname")).getAsString();
                    lastName = Objects.requireNonNull(event.getOption("lastname")).getAsString();
                    email = Objects.requireNonNull(event.getOption("email")).getAsString();
                    address = Objects.requireNonNull(event.getOption("address")).getAsString();
                    earning = Double.parseDouble(Objects.requireNonNull(event.getOption("earning")).getAsString());
                    password = Util.encode(
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

                if (!Util.isValidEmail(email)) {
                    event.reply("invalid email").queue();
                    return;
                }

                User user = new User();

                user.setId(userId);
                user.setTaxCode(taxCode);
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

                logger.info("{} was success registered", userName);

                event.reply("success to register").queue();
                cookie.put(event.getUser().getIdLong(), new Cookie("in", user.getEmail()));
            }
            case "currency" -> {
                try {
                    if (!cookie.get(event.getUser().getIdLong()).getLogin().equals("in")) {
                        event.reply(warn1).queue();
                        return;
                    }
                } catch (Exception e) {
                    event.reply(warn1).queue();
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
                    password = Util.encode(
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
                if (cookie.containsKey(event.getUser().getIdLong()) && (cookie.get(event.getUser().getIdLong()).getLogin().equals("in"))) {

                    double amount;
                    String idba;
                    String currency;
                    String desc;

                    try {
                        amount = Objects.requireNonNull(event.getOption("amount")).getAsDouble();
                        idba = Objects.requireNonNull(event.getOption("idba")).getAsString();
                        currency = Objects.requireNonNull(event.getOption("currency")).getAsString();
                        desc = Objects.requireNonNull(event.getOption("desc")).getAsString();
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

                    if (!accountService.transfer(accountFrom, accountTo, currency, amount, desc)) {
                        event.reply("Not enough money").queue();
                        return;
                    }

                    event.reply("Check accounts").queue();
                    return;
                }
                event.reply(warn1).queue();
            }
            case "account" -> {
                if (cookie.containsKey(event.getUser().getIdLong()) && (cookie.get(event.getUser().getIdLong()).getLogin().equals("in"))) {
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
                event.reply(warn1).queue();
            }
            default -> event.reply("What do u write?").queue();
        }
    }
}
