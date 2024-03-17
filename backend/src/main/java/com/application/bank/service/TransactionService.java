package com.application.bank.service;

import org.springframework.stereotype.Service;

import com.application.bank.model.Account;
import com.application.bank.model.Transaction;
import com.application.bank.repository.AccountRepository;
import com.application.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // public void initiateMoneyTransfer(TransferRequest transferRequest) {
    // // Implement money transfer logic here
    // }

    public List<Transaction> getTransactionHistory(UUID accountId) {
        return transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);
    }

    public void deleteTransactions(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findByFromAccountIdOrToAccountId(accountId, accountId);

        transactions.forEach(transaction -> {

            if (transaction.getToAccount().getId().equals(accountId)) {
                Account account = transaction.getFromAccount();

                BigDecimal newBalance = account.getBalance().add(transaction.getAmount());

                account.setBalance(newBalance);

                accountRepository.save(account);

            } else if (transaction.getFromAccount().getId().equals(accountId)) {
                Account account = transaction.getToAccount();

                BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());

                account.setBalance(newBalance);

                accountRepository.save(account);
            }

            transactionRepository.deleteById(transaction.getId());

        });

    }
}
