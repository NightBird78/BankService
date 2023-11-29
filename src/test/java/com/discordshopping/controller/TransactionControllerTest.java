package com.discordshopping.controller;

import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTransaction() throws Exception {
        TransactionDto expect = new TransactionDto(
                "b88a5714-7aff-1a60-81bd-dca7a4457fb0",
                "transferFunds",
                "morkovka",
                "IDBA9162808856195223",
                "bobr345",
                "IDBA8619893521773869",
                "+400.0",
                "for cookies",
                "2023-09-06T22:26:55"
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/transaction/get/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "b88a5714-7aff-1a60-81bd-dca7a4457fb0")
                        .param("idba", "IDBA8619893521773869"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        TransactionDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });


        assertEquals(expect, actual);
    }

    @Test
    void getAllTransactions() throws Exception {
        List<TransactionDto> expect = List.of(
                new TransactionDto(
                        "b88a5714-7aff-1a60-81bd-dca7a4457fb0",
                        "transferFunds",
                        "morkovka",
                        "IDBA9162808856195223",
                        "bobr345",
                        "IDBA8619893521773869",
                        "-10.0",
                        "for cookies",
                        "2023-09-06T22:26:55"
                ),
                new TransactionDto(
                        "b87a5714-7aff-1a60-81bd-dca7a4457fb0",
                        "transferFunds",
                        "morkovka",
                        "IDBA9162808856195223",
                        "bobr345",
                        "IDBA8619893521773869",
                        "-10.0",
                        "for cookies",
                        "2023-08-29T21:49:09"
                )
        );


        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/transaction/get/all/by-account")
                        .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        List<TransactionDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);
    }

    @Test
    void getAllShortTransactions() throws Exception {

        List<TransactionDtoShort> expected = List.of(
                new TransactionDtoShort("+400.0", "IDBA9162808856195223"),
                new TransactionDtoShort("+400.0", "IDBA9162808856195223")
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/transaction/get/all/by-account/short")
                        .param("id", "8812cda8-f70e-e87d-a8d1-438fc870ed56")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        List<TransactionDtoShort> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expected, actual);

    }
}