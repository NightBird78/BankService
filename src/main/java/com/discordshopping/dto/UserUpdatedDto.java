package com.discordshopping.dto;

import lombok.Data;

@Data
public class UserUpdatedDto {
    String taxCode;
    String nickName;
    String firstName;
    String lastName;
    Double earning;
    String email;
    String address;
}
