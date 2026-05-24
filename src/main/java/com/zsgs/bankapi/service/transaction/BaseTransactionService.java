package com.zsgs.bankapi.service.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.zsgs.bankapi.model.Account;
import com.zsgs.bankapi.model.Transaction;
import com.zsgs.bankapi.repository.AccountRepository;
import com.zsgs.bankapi.repository.TransactionRepository;

public abstract class BaseTransactionService {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected TransactionRepository transactionRepository;

    protected Account getValidatedAccount(String accountNumber) {
        Long accountNum;
        try {
            accountNum = Long.parseLong(accountNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid account number format.");
        }

        return accountRepository.findByAccountNumber(accountNum)
                .orElseThrow(() -> new IllegalArgumentException("Account not found."));
    }

    protected boolean isValidateSufficientBalance(Account account, Double amount) {
        if (account.getBalance() < amount) return false;
        
        return true;
    }

    protected void saveTransactionHistory(Account account, Double amount, String type, String receiverAccountNo, String receiverName) {
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCurrentbalance(account.getBalance());
        transaction.setTransacAmount(amount);
        transaction.setTransacType(type);
        
        if (receiverAccountNo != null && !receiverAccountNo.isEmpty()) {
            try {
                transaction.setTransacAccountNumber(Long.parseLong(receiverAccountNo));
            } catch (NumberFormatException e) {
                transaction.setTransacAccountNumber(0L);
            }
        } else {
            transaction.setTransacAccountNumber(0L);
        }
        
        transaction.setReceiverName(receiverName);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        transaction.setCurrentTime(now.format(timeFormatter));
        transaction.setCurrentDate(now.format(dateFormatter));
        
        transactionRepository.save(transaction);
    }
}
