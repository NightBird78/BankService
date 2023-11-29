package com.discordshopping.entity;

import com.discordshopping.entity.enums.AgreementStatus;
import com.discordshopping.entity.enums.CurrencyCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "agreements")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private UserAccount userAccount;

    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    // default is rate of product
    // every month/year sum will be more on interest
    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private CurrencyCode currencyCode;

    @Column(name = "agreement_status")
    @Enumerated(EnumType.STRING)
    private AgreementStatus agreementStatus;

    // every 1_000$ of sum = discount + 0.1%
    // interest - discount, discount only for it
    @Column(name = "discount_rate")
    private Double discountRate;

    // max of account get money, counting as salary * 10 years
    @Column(name = "agreement_limit")
    private Double agreementLimit;

    // how many account get money, and how many will pay with interest
    // when it = 0.0, status change to "Completed"
    @Column(name = "sum")
    private Double sum;

    @Column(name="original_sum")
    // how many account get money, and how many will pay
    private Double originalSum;

    @Column(name="paid_sum")
    // how much has already been paid
    private Double paidSum;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
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
                ", interestRate=" + interestRate +
                ", currency=" + currencyCode +
                ", agreementStatus=" + agreementStatus +
                ", discountRate=" + discountRate +
                ", agreementLimit=" + agreementLimit +
                ", sum=" + sum +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}