package com.discordshopping.entity;

import com.discordshopping.entity.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code_id")
    private CurrencyCode currencyCode;

    @Column(name = "price")
    public Double price;
}

