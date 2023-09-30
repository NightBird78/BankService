package com.discordshopping.mapper;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User dtoToUser(UserDto dto);

    UserDto userToDto(User user);
}
