package com.discordshopping.entity;

import com.discordshopping.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "debit_account_id", referencedColumnName = "id")
    private UserAccount debitUserAccount;

    @ManyToOne(cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "credit_account_id", referencedColumnName = "id")
    private UserAccount creditUserAccount;

    @Column(name = "transaction_description")
    private String description;


    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(debitUserAccount, that.debitUserAccount) && Objects.equals(creditUserAccount, that.creditUserAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, debitUserAccount, creditUserAccount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", debitAccount=" + debitUserAccount +
                ", creditAccount=" + creditUserAccount +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", createdAt=" + createdAt +
                '}';
    }
}