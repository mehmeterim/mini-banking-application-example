package com.application.bank.service;

import org.springframework.stereotype.Service;

import com.application.bank.DTO.AccountDTO;
import com.application.bank.model.Account;
import com.application.bank.model.User;
import com.application.bank.repository.AccountRepository;
import com.application.bank.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(UUID userId, AccountDTO accountDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Account newAccount = new Account();
        newAccount.setNumber(accountDTO.getNumber());
        newAccount.setName(accountDTO.getName());
        newAccount.setBalance(accountDTO.getBalance());
        newAccount.setUser(user);

        return accountRepository.save(newAccount);
    }

    public List<Account> searchAccounts(UUID userId, String number, String name) {
        if (number != null && name != null) {
            return accountRepository.findByUserIdAndNumberAndName(userId, number, name);
        } else if (number != null) {
            return accountRepository.findByUserIdAndNumber(userId, number);
        } else if (name != null) {
            return accountRepository.findByUserIdAndName(userId, name);
        } else {
            return accountRepository.findByUserId(userId);
        }
    }

    public Account updateAccount(UUID userId, UUID accountId, AccountDTO accountDTO) {
        accountRepository.updateNameAndNumberByUserAndId(userId, accountId, accountDTO.getName(),
                accountDTO.getNumber());

        return null;
    }

    public void deleteAccount(UUID accountId, UUID userId) {
        accountRepository.deleteByIdAndUserId(accountId, userId);
    }

    public Account viewAccountDetails(UUID accountId, UUID userId) {
        return accountRepository.findByIdAndUserId(accountId, userId);
    }

    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }
}
