package com.discordshopping.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    String id;
    String type;
    String from_user;
    String to_user;
    String amount;
    String description;
    String date;
}
