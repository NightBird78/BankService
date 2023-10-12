package com.discordshopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Table(name = "users", schema = "discord_db")
@Entity
public class User {

    @Id
    @Column(name = "discord_id")
    private UUID id;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "user_name")
    private String nickName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "annual_earnings")
    private Double earning;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(taxCode, user.taxCode) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taxCode, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", taxCode='" + taxCode + '\'' +
                ", nickName='" + nickName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
