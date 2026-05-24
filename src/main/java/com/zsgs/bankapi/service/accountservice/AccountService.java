package com.zsgs.bankapi.service.accountservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zsgs.bankapi.dto.AccountCreationRequest;
import com.zsgs.bankapi.model.Account;
import com.zsgs.bankapi.model.User;
import com.zsgs.bankapi.repository.AccountRepository;
import com.zsgs.bankapi.repository.UserRepository;

@Service
public class AccountService implements IAccountService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final int PREFIX = 9102;
    private final Random random = new Random();

    @Transactional
    public Long createAccount(AccountCreationRequest request) {
        if (userRepository.existsByPhoneNumber(request.getMobileNo())) {
            throw new IllegalArgumentException("User with this mobile number already exists.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        // 1. Create and Save User
        User user = new User();
        user.setUserName(request.getName());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getMobileNo());
        user.setEmail(request.getEmail());
        
        user = userRepository.save(user);

        // 2. Generate unique Account Number
        Long accountNumber = generateUniqueAccountNumber();

        // 3. Create and Save Account
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(request.getInitialBalance() != null ? request.getInitialBalance() : 500.0);
        account.setAccountType(request.getAccountType());
        account.setIsActive(true);
        account.setUser(user);
        
        accountRepository.save(account);

        return accountNumber;
    }

    private Long generateAccountNumber() {
        long sb = PREFIX;
        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            sb = (sb * 10) + digit;
        }
        return sb;
    }

    private Long generateUniqueAccountNumber() {
        Long accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    public AccountCreationRequest getAccountReview(String accountNumber) {
        AccountCreationRequest request = accountRepository.getAccountReviewByAccountNumberNo(accountNumber);
        if (request == null) {
            throw new IllegalArgumentException("No account found for the given mobile number.");
        }
        return request;
    }
    public List<AccountCreationRequest> getAllAccountReview() {
        List<AccountCreationRequest> request = accountRepository.getAllAccountReviews();
        if (request == null) {
            throw new IllegalArgumentException("No account found for the given mobile number.");
        }
        return request;
    }
}
