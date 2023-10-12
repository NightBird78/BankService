package com.discordshopping.entity;

import com.discordshopping.entity.enums.CurrencyCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "currencies", schema = "discord_db")
public class Currency {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code_id")
    private CurrencyCode currencyCode;

    @Column(name = "price")
    public Double price;
}

