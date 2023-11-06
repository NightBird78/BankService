package com.discordshopping.entity.dto;

import lombok.Data;

@Data
public class UserCreatedDto {
    String taxCode;
    String nickName;
    String firstName;
    String lastName;
    String earning;
    String email;
    String password;
    String address;
}