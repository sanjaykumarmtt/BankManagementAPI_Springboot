package com.zsgs.bankapi.service.transaction.withdrawservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zsgs.bankapi.dto.TransactionRequest;
import com.zsgs.bankapi.model.Account;
import com.zsgs.bankapi.service.transaction.BaseTransactionService;

@Service
public class WithdrawService extends BaseTransactionService implements IWithdrawService{

    @Transactional
    public Map<String, Object> withdraw(TransactionRequest request) {
        Account account = getValidatedAccount(request.getAccountNumber());

		if (!isValidateSufficientBalance(account, request.getAmount()))
			throw new IllegalArgumentException("Insufficient balance in account: " + account.getAccountNumber());

        account.setBalance(account.getBalance() - request.getAmount());
        accountRepository.save(account);

        saveTransactionHistory(account, -request.getAmount(), "Withdraw", null, null);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Withdrawal successful!");
        response.put("accountNumber", account.getAccountNumber());
        response.put("newBalance", account.getBalance());

        return response;
    }
}
