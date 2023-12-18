package com.discordshopping.mapper;

import com.discordshopping.entity.UserAccount;
import com.discordshopping.dto.AccountDto;
import com.discordshopping.dto.AccountUpdatedDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(source = "user.nickName", target ="nickName")
    @Mapping(source = "accountStatus", target = "status")
    @Mapping(source = "balance", target = "balance")
    @Mapping(source = "currencyCode", target = "currency")
    @Mapping(source = "createdAt", target = "date")
    AccountDto accountToDto(UserAccount account);

    List<AccountDto> accountToDto(List<UserAccount> accounts);

    UserAccount merge(UserAccount from, @MappingTarget UserAccount to);

    UserAccount dtoToAccount(AccountUpdatedDto accountUpdatedDto);
}
