package com.discordshopping.controller;

import com.discordshopping.dto.AgreementCreatedDto;
import com.discordshopping.dto.AgreementDto;
import com.discordshopping.dto.TransactionDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class AgreementControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    void _check(String arg, Double expect) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/agreement/api/discount/check")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("sum", arg))
                .andReturn();

        Double actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);


    }

    @Test
    void check() {

        Map<String, Double> tests = Map.of(
                "2200", 0.2,
                "1500", 0.1,
                "4900", 0.4,
                "10000", 1.0
        );

        tests.forEach((key, value) -> {
            try {
                _check(key, value);
            } catch (Exception e) {
                throw new AssertionFailedError(e.getMessage());
            }
        });
    }


    @Test
    void getAgreement() throws Exception {

        AgreementDto expect = new AgreementDto();

        expect.setId("1f6f6a40-53a1-4a67-a2e0-7bfe4f86d758");
        expect.setNickName("morkovka");
        expect.setProductName("AutoLoans");
        expect.setCurrencyCode("USD");
        expect.setInterestRate("4.7");
        expect.setStatus("Active");
        expect.setSum("4807.5");
        expect.setPaidSum("0.0");
        expect.setDate("2023-05-22T10:30:00");

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/agreement/get/detail")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "1f6f6a40-53a1-4a67-a2e0-7bfe4f86d758"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AgreementDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);

    }

    @Test
    void getAllAgreement() throws Exception {

        List<AgreementDto> expect = new ArrayList<>();

        AgreementDto expectObj = new AgreementDto();

        expectObj.setId("3c8c492a-42a8-46cf-9e0f-6b20b8b72732");
        expectObj.setNickName("bobr345");
        expectObj.setProductName("PersonalLoans");
        expectObj.setCurrencyCode("EUR");
        expectObj.setStatus("Active");
        expectObj.setSum("1432.57");
        expectObj.setInterestRate("2.4");
        expectObj.setPaidSum("0.0");
        expectObj.setDate("2023-11-21T15:45:00");

        expect.add(expectObj);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/agreement/get/all/by-account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "8812cda8-f70e-e87d-a8d1-438fc870ed56"))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        List<AgreementDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expect, actual);
    }

    @Test
    void createAgreement() throws Exception {

        MvcResult transactionOriginal = mockMvc.perform(
                        MockMvcRequestBuilders.get("/transaction/get/all/by-account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0"))
                .andReturn();

        List<TransactionDto> transactionOriginalList = objectMapper.readValue(transactionOriginal.getResponse().getContentAsString(), new TypeReference<>() {
        });

        int transactionCount = transactionOriginalList.size();

        AgreementDto expect = new AgreementDto(
                null,
                "morkovka",
                "PersonalLoans",
                "USD",
                "2.0",
                "Active",
                "5000.0",
                "0.0",
                null
        );

        AgreementCreatedDto create = new AgreementCreatedDto(
                "fa5b432e-17aa-86e0-c190-f98fb20b97b0",
                "3c127808-632b-a56d-6dcb-14135278d9c4",
                "USD",
                "5000.0"
        );


        String createDto = objectMapper.writeValueAsString(create);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/agreement/new")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createDto))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        AgreementDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        expect.setId(actual.getId());

        assertEquals(expect, actual);

        MvcResult transactionActual = mockMvc.perform(
                        MockMvcRequestBuilders.get("/transaction/get/all/by-account")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "fa5b432e-17aa-86e0-c190-f98fb20b97b0"))
                .andReturn();

        List<TransactionDto> transactionActualList = objectMapper.readValue(transactionActual.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(transactionCount + 1, transactionActualList.size());

    }
}