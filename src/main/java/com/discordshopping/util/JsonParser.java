package com.discordshopping.util;

import com.discordshopping.bot.util.Constant;
import com.discordshopping.entity.Currency;
import com.discordshopping.entity.CurrencyParsed;
import com.discordshopping.entity.enums.CurrencyCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonParser {

    public static String getAllCurrency() {
        URL url;
        try {
            url = new URL(Constant.CurrencyAPI);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String jsonstring;
        try {
            jsonstring = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return String.join(",\n", jsonstring.split(","));
    }

    public static Map<CurrencyCode, Double> parseCurrency() throws JsonProcessingException {
        List<CurrencyParsed> currencies = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> dataMap = objectMapper
                .readValue(JsonParser.getAllCurrency(), new TypeReference<>() {
                });

        for (Map<String, Object> map : dataMap) {
            String[] strings = map.get("effectiveDate").toString().split("-");
            LocalDateTime localDateTime = LocalDateTime.of(
                    Integer.parseInt(strings[0]),
                    Integer.parseInt(strings[1]),
                    Integer.parseInt(strings[2]),
                    0,
                    0);
            List<Map<String, Object>> currencyList = objectMapper
                    .readValue(map.get("rates").toString()
                                    .replace("{", "{\"")
                                    .replace("=", "\":\"")
                                    .replace(",", "\",")
                                    .replace("}", "\"}")
                                    .replace(", ", ", \"")
                                    .replace("\"}\", \"", "\"}, "),
                            new TypeReference<>() {
                            });
            for (Map<String, Object> currencyMap : currencyList) {
                CurrencyCode c;
                try {
                    c = CurrencyCode.valueOf(currencyMap.get("code").toString());
                } catch (Exception ignore) {
                    continue;
                }
                currencies.add(new CurrencyParsed(
                        localDateTime,
                        c,
                        Double.parseDouble(currencyMap.get("mid").toString())));
            }
        }
        return currencies.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .map(CurrencyParsed::getCurrency)
                .collect(Collectors.toMap(Currency::getCurrencyCode, Currency::getPrice));
    }
}