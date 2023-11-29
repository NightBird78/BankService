package com.discordshopping.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class PrettyPrinter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object print(Object o, Integer... pad) {

        if (o == null) {
            return "null";
        }

        try {
            List<Object> lo = (List<Object>) o;

            List<Object> objList = new ArrayList<>();

            Integer padding = 1;
            Integer plusPad = 1;

            if (pad.length == 1) {
                plusPad = pad[0];
            }

            String spacePad = " ".repeat(Math.max(1, plusPad));

            for (Object object : lo) {
                objList.add(print(object, padding + plusPad));
            }

            return "[" + String.join(", \n\n" + spacePad, objList.stream().map(Object::toString).toList()) + "]";
        } catch (Throwable ignore) {
        }

        try {
            Map<Object, Object> mo = (Map<Object, Object>) o;

            List<Object> list = mo.keySet().stream().toList();

            List<Object> objList = new ArrayList<>();

            Integer plusPad = 0;

            if (pad.length > 0) {
                plusPad = pad[0];
            }

            Integer max = list.stream().map(Object::toString).map(String::length).max(Integer::compareTo).orElse(0);

            for (Object object : list) {
                boolean valueMap = false;

                try {
                    Map<Object, Object> mo2 = (Map<Object, Object>) mo.get(object);
                    valueMap = true;
                } catch (Throwable ignore) {
                }
                String enter = "";
                if (valueMap) {
                    enter = "\n" + " ".repeat(max + plusPad + 3);
                }

                String repeat = " ".repeat(max - object.toString().length());
                if (mo.get(object) != null) {

                    objList.add(String.format("{%s :%s %s}",
                            print(object) + repeat,
                            enter,
                            print(mo.get(object), enter.length())));
                } else {
                    objList.add(String.format("{%s : %s}",
                            print(object) + repeat,
                            print(mo.get(object), enter.length())));
                }
            }

            return String.join(",\n" + " ".repeat(plusPad), objList.stream().map(Object::toString).toList());
        } catch (Throwable ignore) {
        }

        if (o.toString().hashCode() == o.hashCode()) {
            return o.toString();
        }

        Map<Object, Object> map = null;

        try {
            map = objectMapper.readValue(
                    objectMapper.writeValueAsString(o),
                    new TypeReference<>() {
                    }
            );
        } catch (Throwable ignore) {
            return o.toString();
        }

        return print(map);
    }
}