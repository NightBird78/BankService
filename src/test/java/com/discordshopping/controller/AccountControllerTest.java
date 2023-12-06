package com.discordshopping.controller;

import com.discordshopping.dto.AccountDto;
import com.discordshopping.dto.AccountUpdatedDto;
import com.discordshopping.dto.TransactionDto;
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
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @Test
    void getByUser() throws Exception {

        AccountDto expect = new AccountDto();

        expect.setId("fa5b432e-17aa-86e0-c190-f98fb20b97b0");
        expect.setBalance("0.0");
        expect.setNickName("morkovka");
        expect.setDate("2023-10-14T20:01:03");
        expect.setStatus("Active");
        expect.setIdba("IDBA9162808856195223");
        expect.setCurrency("UAH");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get/by-user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "cb09db30-ce1d-467d-85b3-60574a0333ac"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);

    }

    @Test
    void getAccount() throws Exception {
        AccountDto expect = new AccountDto();

        expect.setId("fa5b432e-17aa-86e0-c190-f98fb20b97b0");
        expect.setBalance("0.0");
        expect.setNickName("morkovka");
        expect.setDate("2023-10-14T20:01:03");
        expect.setStatus("Active");
        expect.setIdba("IDBA9162808856195223");
        expect.setCurrency("UAH");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);
    }

    @Test
    void update() throws Exception {

        AccountDto expect = new AccountDto();

        expect.setId("fa5b432e-17aa-86e0-c190-f98fb20b97b0");
        expect.setBalance("0.0");
        expect.setNickName("morkovka");
        expect.setDate("2023-10-14T20:01:03");
        expect.setStatus("InActive");
        expect.setIdba("IDBA9162808856195223");
        expect.setCurrency("UAH");

        AccountUpdatedDto update = new AccountUpdatedDto();
        update.setAccountStatus("InActive");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/account/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0")
                                .content(objectMapper.writeValueAsBytes(update)))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);
    }

    @Test
    void transfer() throws Exception {

        // ----- transfer ----- \\
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/account/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("from", "IDBA8619893521773869")
                                .param("to", "IDBA9162808856195223")
                                .param("desc", "for cookie")
                                .param("code", "EUR")
                                .param("amount", "10")
                                .param("type", "transferFunds"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        TransactionDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(-10, Double.parseDouble(actual.getAmount()));

        // ----- check debit account ----- \\
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "8812cda8-f70e-e87d-a8d1-438fc870ed56"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actualD = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(90, Double.parseDouble(actualD.getBalance()));

        // ----- check credit account ----- \\
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/account/get")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AccountDto actualC = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(400, Double.parseDouble(actualC.getBalance()));
    }
}