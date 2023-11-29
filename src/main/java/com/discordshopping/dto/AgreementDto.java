package com.discordshopping.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDto {
    String id;
    String nickName;
    String productName;
    String currencyCode;
    String interestRate;
    String status;
    String sum;
    String paidSum;
    String date;
}
