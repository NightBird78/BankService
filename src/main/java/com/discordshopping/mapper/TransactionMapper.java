package com.discordshopping.mapper;

import com.discordshopping.entity.Transaction;
import com.discordshopping.entity.dto.TransactionDto;
import com.discordshopping.entity.dto.TransactionDtoShort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.slf4j.LoggerFactory;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.transactionType", target = "type")
    @Mapping(source = "transaction.debitUserAccount.user.nickName", target = "from_user")
    @Mapping(source = "transaction.creditUserAccount.user.nickName", target = "to_user")
    @Mapping(target = "amount", expression = "java(getAmount(transaction, idba))")
    @Mapping(source = "transaction.createdAt", target = "date")
    TransactionDto transactionToDto(Transaction transaction, String idba);


    @Mapping(target = "amount", expression = "java(getAmount(transaction, idba))")
    @Mapping(source = "idba", target = "IDBA")
    TransactionDtoShort transactionToShortDto(Transaction transaction, String idba);

    default String getAmount(Transaction transaction, String idba) {
        return transaction.getCreditUserAccount().getIdba().equals(idba) ?
                "+" + transaction.getAmountTo().toString() :
                "-" + transaction.getAmountFrom().toString();
    }
}
