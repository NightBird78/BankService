package com.discordshopping.mapper;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.entity.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "user.nickName", target ="nickName")
    @Mapping(source = "accountStatus", target = "status")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currencyCode", target = "currency")
    @Mapping(source = "createdAt", target = "date")
    AccountDto accountToDto(UserAccount account);

}
