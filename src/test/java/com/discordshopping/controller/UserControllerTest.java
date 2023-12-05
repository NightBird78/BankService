package com.discordshopping.controller;

import com.discordshopping.dto.UserCreatedDto;
import com.discordshopping.dto.UserDto;
import com.discordshopping.dto.UserUpdatedDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/drop.sql")
@Sql("/create.sql")
@Sql("/insert.sql")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void create() throws Exception {

        UserCreatedDto create = new UserCreatedDto();
        create.setTaxCode("ABGND");
        create.setNickName("harry_potter");
        create.setFirstName("Willia");
        create.setLastName("Rodriguez");
        create.setEarning("15000");
        create.setAddress("Benin Saltown Anderson Locks 41258");
        create.setPassword("8664bb1a-cde2-44c8-95ed-4290ac4b694b");

        UserDto expect = new UserDto(
                null,
                "harry_potter",
                "Willia",
                "Rodriguez",
                "hal.oreilly@yahoo.com",
                null
        );

        String jsonDto = objectMapper.writeValueAsString(create);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/new")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto))
                .andReturn();


        assertEquals(400, mvcResult.getResponse().getStatus());

        create.setEmail("antonM@re.co");


        jsonDto = objectMapper.writeValueAsString(create);
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/new")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());

        create.setEmail("hal.oreilly@yahoo.com");

        jsonDto = objectMapper.writeValueAsString(create);
        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/new")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonDto))
                .andReturn();

        UserDto responseEntity = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        expect.setCreatedAt(responseEntity.getCreatedAt());
        expect.setId(responseEntity.getId());

        assertEquals(expect, responseEntity);
    }


    @Test
    void get() throws Exception {

        UserDto expect = new UserDto(
                "cb09db30-ce1d-467d-85b3-60574a0333ac",
                "morkovka",
                "Anton",
                "myrashka",
                "antonM@re.co",
                "2014-07-23T18:48:56"
        );


        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/user/get")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "cb09db30-ce1d-467d-0000-60574a0333ac"))
                .andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());


        mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.get("/user/get")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "cb09db30-ce1d-467d-85b3-60574a0333ac"))
                .andReturn();

        UserDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(expect, actual);

    }

    @Test
    void update() throws Exception {
        UserDto expect = new UserDto(
                "cb09db30-ce1d-467d-85b3-60574a0333ac",
                "morkovka",
                "Anton",
                "zhyk",
                "antonM@re.co",
                "2014-07-23T18:48:56"
        );
        UserUpdatedDto update = new UserUpdatedDto();

        update.setLastName("zhyk");

        String updateJson = objectMapper.writeValueAsString(update);

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/update")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("id", "cb09db30-ce1d-467d-85b3-60574a0333ac")
                                .content(updateJson))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());

        UserDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {
                });

        assertEquals(expect, actual);

    }
}