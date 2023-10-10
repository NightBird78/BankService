package com.discordshopping.entity;

import com.discordshopping.entity.enums.AccountStatus;
import com.discordshopping.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.CascadeType.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "accounts", schema = "discord_db")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = {MERGE, PERSIST, REFRESH})
    @JoinColumn(name = "client_id", referencedColumnName = "discord_id")
    private User user;

    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "currency_code")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

//    @OneToMany(cascade = {MERGE, REFRESH, PERSIST})
    @OneToMany
    private Set<Transaction> transactions;

//    @OneToMany(cascade = {MERGE, REFRESH, PERSIST})
    @OneToMany
    private Set<Agreement> agreements;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", user=" + user +
                ", accountStatus=" + accountStatus +
                ", balance=" + balance +
                ", currency=" + currency +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", transactions=" + transactions +
                ", agreements=" + agreements +
                '}';
    }
}