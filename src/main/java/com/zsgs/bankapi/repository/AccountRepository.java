package com.zsgs.bankapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.zsgs.bankapi.dto.AccountCreationRequest;
import com.zsgs.bankapi.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	
    boolean existsByAccountNumber(Long accountNumber);
    Optional<Account> findByAccountNumber(Long accountNumber);
    
    @Query("SELECT new com.zsgs.bankapi.dto.AccountCreationRequest(u.userId,a.accountNumber,u.userName, u.phoneNumber, u.email, u.address, a.accountType, a.balance) " +
           "FROM Account a JOIN a.user u WHERE a.accountNumber = :accountNumber")
    AccountCreationRequest getAccountReviewByAccountNumberNo(@Param("accountNumber") String accountNumber);
    
    @Query("SELECT new com.zsgs.bankapi.dto.AccountCreationRequest(u.userId, a.accountNumber,u.userName, u.phoneNumber, u.email, u.address, a.accountType, a.balance) " +
            "FROM Account a JOIN a.user u")
     List<AccountCreationRequest> getAllAccountReviews();
}
