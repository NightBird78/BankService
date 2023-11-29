package com.discordshopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementCreatedDto {
    String accountId;
    String productId;
    String currencyCode;
    String sum;
}