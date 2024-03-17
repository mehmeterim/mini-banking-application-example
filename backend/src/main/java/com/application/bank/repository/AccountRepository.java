package com.application.bank.repository;

import org.hibernate.Remove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.bank.model.Account;
import com.application.bank.model.User;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUserId(UUID userId);

    List<Account> findByUserIdAndNumber(UUID userId, String number);

    List<Account> findByUserIdAndName(UUID userId, String name);

    List<Account> findByUserIdAndNumberAndName(UUID userId, String number, String name);

    Account findByIdAndUserId(UUID id, UUID userId);

    @Transactional
    List<Account> deleteByIdAndUserId(UUID id, UUID userId);

    @Modifying
    @Query("update accounts a set a.number = ?1, a.name = ?2 where a.id = ?3 and a.user = ?4")
    int setNumberAndName(String number, String name, UUID id, User user);

    @Transactional
    @Modifying
    @Query("UPDATE accounts a SET a.name = :name, a.number = :number WHERE a.user.id = :userId AND a.id = :accountId")
    int updateNameAndNumberByUserAndId(@Param("userId") UUID userId, @Param("accountId") UUID accountId,
            @Param("name") String name, @Param("number") String number);

}
