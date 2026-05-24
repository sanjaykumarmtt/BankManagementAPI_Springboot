package com.zsgs.bankapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.zsgs.bankapi.model.Transaction;
import com.zsgs.bankapi.dto.TransactionHistoryResponse;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT new com.zsgs.bankapi.dto.TransactionHistoryResponse(u.userName, a.accountNumber, t.id, t.transactionId, t.currentbalance, t.transacAmount, t.transacType, t.transacAccountNumber, t.receiverName, t.currentTime, t.currentDate) FROM Transaction t JOIN t.account a JOIN a.user u")
    List<TransactionHistoryResponse> findAllTransactionHistory();

    @Query("SELECT new com.zsgs.bankapi.dto.TransactionHistoryResponse(u.userName, a.accountNumber, t.id, t.transactionId, t.currentbalance, t.transacAmount, t.transacType, t.transacAccountNumber, t.receiverName, t.currentTime, t.currentDate) FROM Transaction t JOIN t.account a JOIN a.user u WHERE a.accountNumber = :accountNumber")
    List<TransactionHistoryResponse> findTransactionHistoryByAccountNumber(@Param("accountNumber") Long accountNumber);
}
