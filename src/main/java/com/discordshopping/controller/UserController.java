package com.discordshopping.controller;

import com.discordshopping.validation.annotation.NullField;
import com.discordshopping.validation.annotation.ValidUUID;
import com.discordshopping.dto.UserCreatedDto;
import com.discordshopping.dto.UserDto;
import com.discordshopping.dto.UserUpdatedDto;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/update")
    public ResponseEntity<UserDto> update(@ValidUUID @Param("id") String id, @RequestBody UserUpdatedDto uuDto) {
        return new ResponseEntity<>(userService.merge(uuDto, id), HttpStatus.OK);
    }

    @PostMapping(value = "/new")
    public ResponseEntity<UserDto> create(@NullField @RequestBody UserCreatedDto dto){
        return new ResponseEntity<>(userService.create(dto), HttpStatus.OK);
    }
}