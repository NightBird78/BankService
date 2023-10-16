package com.discordshopping.mapper;

import com.discordshopping.entity.User;
import com.discordshopping.entity.dto.UserDto;
import com.discordshopping.entity.dto.UserUpdatedDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User dtoToUser(UserDto dto);

    User dtoToUser(UserUpdatedDto user);

    UserDto userToDto(User user);

    User merge(User from,@MappingTarget User to);

    UserUpdatedDto fullDto(User user);
}
