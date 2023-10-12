package com.discordshopping.service;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> getById(String id);

    User create(UserDto userDto);
    boolean create(User user);

    boolean existByPasswordAndEmail(String password, String email);

    boolean existByEmail(String email);

    boolean existById(UUID id);
}
