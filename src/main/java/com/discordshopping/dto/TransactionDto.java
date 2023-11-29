package com.discordshopping.dto;

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
    String from_idba;
    String to_user;
    String to_idba;
    String amount;
    String description;
    String date;
}
