package com.discordshopping.service.repository;

import com.discordshopping.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query("select u from UserAccount u where u.idba = :idba")
    Optional<UserAccount> findByIDBA(@Param("idba") String idba);

    @Query("select u from UserAccount u where u.user.id = :id")
    Optional<UserAccount> findByUserId(@Param("id") UUID id);

    @Query("select u from UserAccount u where u.user.email = :email")
    Optional<UserAccount> findByEmail(@Param("email") String email);
}
