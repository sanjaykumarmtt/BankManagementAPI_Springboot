package com.zsgs.bankapi.service.transaction.depositservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zsgs.bankapi.dto.TransactionRequest;
import com.zsgs.bankapi.model.Account;
import com.zsgs.bankapi.service.transaction.BaseTransactionService;

@Service
public class DepositService extends BaseTransactionService implements IDepositService {

    @Transactional
    public Map<String, Object> deposit(TransactionRequest request) {
        Account account = getValidatedAccount(request.getAccountNumber());

        account.setBalance(account.getBalance() + request.getAmount());
        accountRepository.save(account);

        saveTransactionHistory(account, request.getAmount(), "Deposit", null, null);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Deposit successful!");
        response.put("accountNumber", account.getAccountNumber());
        response.put("newBalance", account.getBalance());

        return response;
    }
}
