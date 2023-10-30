package com.discordshopping.controller;

import com.discordshopping.bot.util.ValidUUID;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.entity.dto.UserUpdatedDto;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getUserResponse(@ValidUUID @PathVariable("id") String id) {
        return new ResponseEntity<>(userService.findDtoById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<UserUpdatedDto> update(@ValidUUID @PathVariable("id") String id, @RequestBody UserUpdatedDto uuDto) {
        return new ResponseEntity<>(userService.merge(uuDto, id), HttpStatus.OK);
    }
}