package com.discordshopping.util;

import com.discordshopping.bot.util.Constant;
import org.apache.commons.io.IOUtils;
import org.hibernate.sql.exec.ExecutionException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static List<String> miniParse(String s) {

        List<List<Integer>> listList = new ArrayList<>();

        List<Integer> integerList = new ArrayList<>();

        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '{') {
                count++;
                if (count == 1) {
                    integerList.add(i);
                }
            } else if (s.charAt(i) == '}') {
                count--;

                if (count == 0) {
                    integerList.add(i);
                    listList.add(List.copyOf(integerList));
                    integerList.clear();
                }
                if (count < 0) {
                    throw new ExecutionException("invalid json format");
                }
            }
        }

        if (count > 0) {
            throw new ExecutionException("invalid json format");
        }

        List<String> strings = new ArrayList<>();
        listList.forEach(l -> {
            int x1 = l.get(0);
            int x2 = l.get(1);

            strings.add(s.substring(x1, x2 + 1));
        });
        return strings;
    }

    public static Map<String, Double> parseCurrencyToMap() {
        String jsonCaver = JsonParser.getAllCurrency();

        List<String> currencyGroup = new ArrayList<>();

        JsonParser.miniParse(jsonCaver).forEach(l ->
                JsonParser.miniParse(l.substring(1, l.length() - 1))
                        .forEach(l1 -> currencyGroup.add(l1.substring(1, l1.length() - 1))));
        Map<String, Double> map = new HashMap<>();

        for (int i = currencyGroup.size() - 1; i > 0; i--) {

            String s = currencyGroup.get(i);

            String[] array = s.replace("\"", "").split(",");

            String code = array[1].split(":")[1];
            Double price = Double.parseDouble(array[2].split(":")[1]);

            if (!map.containsKey(code)) {
                map.put(code, price);
            }
        }
        return map;
    }
}
