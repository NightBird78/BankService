package com.discordshopping.controller;

import com.discordshopping.dto.ProductDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getProduct() throws Exception {
        ProductDto expected = new ProductDto();
        expected.setId("3c127808-632b-a56d-6dcb-14135278d9c4");
        expected.setDate("1999-11-27T01:43:37");
        expected.setType("PersonalLoans");
        expected.setStatus("Available");
        expected.setInterestRate("2.5");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product/get/detail")
                        .param("id", "3c127808-632b-a56d-6dcb-14135278d9c4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        ProductDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expected, actual);

    }

    @Test
    void getAllProduct() throws Exception {
        List<ProductDto> expected = List.of(
                new ProductDto(
                        "3c127808-632b-a56d-6dcb-14135278d9c4",
                        "PersonalLoans",
                        "Available",
                        "2.5",
                        "1999-11-27T01:43:37"
                ),
                new ProductDto(
                        "691b92c4-6c5a-e6e7-d7fe-d18e7bf4d931",
                        "AutoLoans",
                        "Available",
                        "5.0",
                        "2000-05-02T20:15:08"
                )
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/product/get/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        List<ProductDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expected, actual);
    }
}