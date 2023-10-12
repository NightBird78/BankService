package com.discordshopping.mapper;

import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transactionType",target = "type")
    @Mapping(source = "debitUserAccount.user.nickName", target = "from_user")
    @Mapping(source = "creditUserAccount.user.nickName", target = "to_user")
    @Mapping(source = "createdAt", target = "date")
    TransactionDto transactionToDto(Transaction transaction);
}
