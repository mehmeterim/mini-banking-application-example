package com.application.bank.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.bank.DTO.AccountDTO;
import com.application.bank.DTO.ResponseDTO;
import com.application.bank.model.Account;
import com.application.bank.service.AccountService;
import com.application.bank.service.TransactionService;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(Authentication authentication, @RequestBody AccountDTO accountDTO) {
        String userId = (String) authentication.getPrincipal();
        Account newAccount = accountService.createAccount(UUID.fromString(userId), accountDTO);

        return ResponseEntity.ok(ResponseDTO.success("Account created successfully!", newAccount));
    }

    @GetMapping
    public ResponseEntity<?> searchAccounts(Authentication authentication,
            @RequestParam(required = false) String number, @RequestParam(required = false) String name) {

        String userId = (String) authentication.getPrincipal();

        List<Account> accounts = accountService.searchAccounts(UUID.fromString(userId), number, name);

        return ResponseEntity.ok(ResponseDTO.success(null, accounts));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(Authentication authentication, @PathVariable String id,
            @RequestBody AccountDTO accountDTO) {

        Optional<Account> account = accountService.findById(UUID.fromString(id));

        if (account.isPresent() == false) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Account not found"));
        }

        String userId = (String) authentication.getPrincipal();

        try {
            Account updatedAccount = accountService.updateAccount(UUID.fromString(userId), UUID.fromString(id),
                    accountDTO);

            return ResponseEntity.ok(ResponseDTO.success("Account updated successfully!", updatedAccount));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(Authentication authentication, @PathVariable String id) {
        transactionService.deleteTransactions(UUID.fromString(id));

        String userId = (String) authentication.getPrincipal();
        accountService.deleteAccount(UUID.fromString(id), UUID.fromString(userId));

        return ResponseEntity.ok(ResponseDTO.success("Account deleted successfully!", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewAccountDetails(Authentication authentication, @PathVariable String id) {
        String userId = (String) authentication.getPrincipal();
        Account account = accountService.viewAccountDetails(UUID.fromString(id), UUID.fromString(userId));

        return ResponseEntity.ok(ResponseDTO.success(null, account));
    }
}
