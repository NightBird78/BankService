package com.discordshopping.entity;

import com.discordshopping.entity.enums.AgreementStatus;
import com.discordshopping.entity.enums.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "agreements", schema = "discord_db")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @JsonIgnore
    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @JsonIgnore
    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "agreement_status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "agreement_limit")
    private Double agreementLimit;

    @Column(name = "sum")
    private Double sum;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agreement agreement)) return false;
        return Objects.equals(id, agreement.id) && Objects.equals(userAccount, agreement.userAccount) && Objects.equals(product, agreement.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccount, product);
    }

    @Override
    public String toString() {
        return "Agreement{" +
                "id=" + id +
                ", account=" + userAccount +
                ", product=" + product +
                ", interestRate=" + interestRate +
                ", currency=" + currency +
                ", agreementStatus=" + agreementStatus +
                ", discountRate=" + discountRate +
                ", agreementLimit=" + agreementLimit +
                ", sum=" + sum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}