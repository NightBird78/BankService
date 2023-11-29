package com.discordshopping.mapper;

import com.discordshopping.entity.Transaction;
import com.discordshopping.dto.TransactionDto;
import com.discordshopping.dto.TransactionDtoShort;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Objects;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "transaction.transactionType", target = "type")
    @Mapping(source = "transaction.debitUserAccount.user.nickName", target = "from_user")
    @Mapping(source = "transaction.creditUserAccount.user.nickName", target = "to_user")
    @Mapping(target = "amount", expression = "java(getAmount(transaction, idba))")
    @Mapping(source = "transaction.createdAt", target = "date")
    @Mapping(source = "transaction.debitUserAccount.idba", target = "from_idba")
    @Mapping(source = "transaction.creditUserAccount.idba", target = "to_idba")
    TransactionDto transactionToDto(Transaction transaction, String idba);


    @Mapping(target = "amount", expression = "java(getAmount(transaction, idba))")
    @Mapping(target = "IDBA", expression = "java(getIdba(transaction, idba))")
    TransactionDtoShort transactionToShortDto(Transaction transaction, String idba);

    default String getAmount(Transaction transaction, String idba) {
        return transaction.getCreditUserAccount().getIdba().equals(idba) ?
                "+" + transaction.getAmountTo().toString() :
                "-" + transaction.getAmountFrom().toString();
    }

    default String getIdba(Transaction transaction, String idba) {
        if (!Objects.equals(transaction.getDebitUserAccount().getIdba(), idba)) {
            return transaction.getDebitUserAccount().getIdba();
        }
        return transaction.getCreditUserAccount().getIdba();
    }
}
