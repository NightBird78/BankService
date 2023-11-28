package com.discordshopping.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDtoShort {
    private String amount;
    private String IDBA;
}
