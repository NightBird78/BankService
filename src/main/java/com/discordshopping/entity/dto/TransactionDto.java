package com.discordshopping.entity.dto;

import lombok.Data;

@Data
public class TransactionDto {
    String id;
    String type;
    String from_user;
    String to_user;
    String amount;
    String description;
    String date;
}
