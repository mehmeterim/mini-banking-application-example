package com.application.bank.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.bank.DTO.ResponseDTO;
import com.application.bank.DTO.TransferDTO;
import com.application.bank.enums.TransactionStatus;
import com.application.bank.model.Account;
import com.application.bank.model.Transaction;
import com.application.bank.repository.AccountRepository;
import com.application.bank.repository.TransactionRepository;
import com.application.bank.service.TransactionService;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository,
            TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    @Transactional
    public ResponseEntity<?> initiateMoneyTransfer(@RequestBody TransferDTO transferDTO) {

        if (transferDTO.getFromAccountId().equals(transferDTO.getToAccountId())) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("The accounts cannot be the same!"));
        }

        Optional<Account> fromAccount = accountRepository.findById(UUID.fromString(transferDTO.getFromAccountId()));

        if (fromAccount.isPresent() == false) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("From account not found"));
        }

        Optional<Account> toAccount = accountRepository.findById(UUID.fromString(transferDTO.getToAccountId()));

        if (toAccount.isPresent() == false) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("To account not found"));
        }

        BigDecimal fromAccountBalance = fromAccount.get().getBalance();
        BigDecimal toAccountBalance = toAccount.get().getBalance();
        BigDecimal amount = transferDTO.getAmount();

        if (fromAccountBalance.compareTo(amount) < 0) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Insufficient funds in the from account"));
        }

        fromAccount.get().setBalance(fromAccountBalance.subtract(amount));
        toAccount.get().setBalance(toAccountBalance.add(amount));

        accountRepository.save(fromAccount.get());
        accountRepository.save(toAccount.get());

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount.get());
        transaction.setToAccount(toAccount.get());
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok(ResponseDTO.success("Money transfer initiated successfully!", null));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> viewTransactionHistory(Authentication authentication, @PathVariable String accountId) {
        String userId = (String) authentication.getPrincipal();

        Account account = accountRepository.findByIdAndUserId(UUID.fromString(accountId), UUID.fromString(userId));

        if (account == null) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Account not found"));
        }

        List<Transaction> transactions = transactionService.getTransactionHistory(UUID.fromString(accountId));

        return ResponseEntity.ok(ResponseDTO.success(null, transactions));
    }
}
