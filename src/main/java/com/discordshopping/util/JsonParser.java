package com.discordshopping.util;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.CurrencyParsed;
import com.discordshopping.entity.enums.CurrencyCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class JsonParser {

    static final ObjectMapper objectMapper = new ObjectMapper();

    public static String getAllCurrency(String bankAPI) {
        URL url;
        try {
            url = new URL(bankAPI);
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

    public static Map<CurrencyCode, Double> parseCurrency(String bankAPI) throws JsonProcessingException {
        List<CurrencyParsed> currencies = new ArrayList<>();
        List<Map<String, Object>> dataMap = objectMapper
                .readValue(JsonParser.getAllCurrency(bankAPI), new TypeReference<>() {
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

    public static Map<String, String> parseConfig(String filename) throws FileNotFoundException {

        StringBuilder string = new StringBuilder();

        try (FileReader fileReader = new FileReader(filename)) {

            int bit;

            while ((bit = fileReader.read()) != -1){
                string.append((char) bit);
            }

            return objectMapper.readValue(string.toString(), new TypeReference<>() {});

        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw (FileNotFoundException) e;
            }
            else {
                Logger logger = LoggerFactory.getLogger(JsonParser.class);

                logger.error(e.getMessage());
                return null;
            }
        }
    }
}