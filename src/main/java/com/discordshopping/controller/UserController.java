package com.discordshopping.controller;

import com.discordshopping.bot.util.validator.annotation.NullField;
import com.discordshopping.bot.util.validator.annotation.ValidUUID;
import com.discordshopping.entity.dto.UserCreatedDto;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.entity.dto.UserUpdatedDto;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUserResponse(@ValidUUID @Param("id") String id) {
        return new ResponseEntity<>(userService.findDtoById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<UserDto> update(@ValidUUID @Param("id") String id, @RequestBody UserUpdatedDto uuDto) {
        return new ResponseEntity<>(userService.merge(uuDto, id), HttpStatus.OK);
    }

    @RequestMapping(value = "/new", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<UserDto> create(@NullField @RequestBody UserCreatedDto dto){
        return new ResponseEntity<>(userService.create(dto), HttpStatus.OK);
    }
}