package com.discordshopping.service.repository;

import com.discordshopping.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<UserAccount, UUID> {
}
