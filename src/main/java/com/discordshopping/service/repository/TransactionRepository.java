package com.discordshopping.service.repository;

import com.discordshopping.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("select t from Transaction t where t.creditUserAccount.id = :id or t.debitUserAccount.id =:id")
    List<Transaction> findAllByAccountId(@Param("id") UUID id);

    @Query("select true from Transaction t where t.creditUserAccount.idba = :idba or t.debitUserAccount.idba = :idba")
    List<Transaction> findAllByIdba(@Param("idba") String idba);
}
