package com.discordshopping.service;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;

import java.util.Optional;

public interface UserService {

    Optional<User> getById(Long id);

    User create(UserDto userDto);
    boolean create(User user);
}
