package com.discordshopping.controller;

import com.discordshopping.entity.Currency;
import com.discordshopping.entity.enums.CurrencyCode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCurrency() throws Exception {

        Currency expect = new Currency(CurrencyCode.EUR, 4.628);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/currency/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("name", "EUR"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        Currency actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);

    }
}