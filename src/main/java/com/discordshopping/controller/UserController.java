package com.discordshopping.controller;

import com.discordshopping.bot.util.MiniUtil;
import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.entity.dto.UserUpdatedDto;
import com.discordshopping.exception.InvalidUUIDException;
import com.discordshopping.exception.NotFoundException;
import com.discordshopping.exception.enums.ErrorMessage;
import com.discordshopping.mapper.UserMapper;
import com.discordshopping.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> getUserResponse(@PathVariable("id") String id) {
        if (!MiniUtil.isValidUUID(id)) {
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }
        Optional<User> opt = userService.getById(id);

        if (opt.isEmpty()) {
            throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
        }
        UserDto userDto = userMapper.userToDto(opt.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @RequestMapping(value = "/update/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<UserUpdatedDto> update(
            @PathVariable("id") String id,
            @RequestBody UserUpdatedDto uuDto) {

        if (!MiniUtil.isValidUUID(id)) {
            throw new InvalidUUIDException(ErrorMessage.INVALID_UUID_FORMAT);
        }
        Optional<User> opt = userService.getById(id);

        if (opt.isEmpty()) {
            throw new NotFoundException(ErrorMessage.DATA_NOT_FOUND);
        }

        User user = userMapper.merge(userMapper.dtoToUser(uuDto), opt.get());

        userService.save(user);

        return new ResponseEntity<>(userMapper.fullDto(user), HttpStatus.OK);
    }

}