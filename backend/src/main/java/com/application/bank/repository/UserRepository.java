package com.application.bank.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.bank.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
