package com.discordshopping.service;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.entity.dto.UserUpdatedDto;

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

    UserUpdatedDto merge(UserUpdatedDto uuDto, String id);
}
