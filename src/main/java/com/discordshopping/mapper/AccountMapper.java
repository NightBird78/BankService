package com.discordshopping.mapper;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import com.discordshopping.entity.dto.AccountUpdatedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.nickName", target ="nickName")
    @Mapping(source = "accountStatus", target = "status")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currencyCode", target = "currency")
    @Mapping(source = "createdAt", target = "date")
    AccountDto accountToDto(UserAccount account);

    UserAccount merge(UserAccount from, @MappingTarget UserAccount to);

    UserAccount dtoToAccount(AccountUpdatedDto accountUpdatedDto);
}
