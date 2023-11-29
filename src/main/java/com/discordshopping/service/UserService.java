package com.discordshopping.service;

import com.discordshopping.entity.User;
import com.discordshopping.dto.UserCreatedDto;
import com.discordshopping.dto.UserDto;
import com.discordshopping.dto.UserUpdatedDto;

import java.util.UUID;

public interface UserService {

    User findById(String id);

    User create(UserDto userDto);

    boolean create(User user);

    boolean existByPasswordAndEmail(String password, String email);

    boolean existByEmail(String email);

    boolean existById(UUID id);

    void save(User user);

    UserDto findDtoById(String id);

    UserDto merge(UserUpdatedDto uuDto, String id);

    UserDto create(UserCreatedDto dto);
}
