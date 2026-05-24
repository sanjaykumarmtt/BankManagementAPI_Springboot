package com.zsgs.bankapi.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "currentbalance", nullable = false)
    private Double currentbalance;

    @Column(name = "transac_amount", nullable = false)
    private Double transacAmount;

    @Column(name = "transac_type", nullable = false)
    private String transacType;

    @Column(name = "transac_account_number", nullable = false)
    private Long transacAccountNumber;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "transaction_time", nullable = false)
    private String currentTime;

    @Column(name = "transaction_date", nullable = false)
    private String currentDate;

    @PrePersist
    protected void onCreate() {
        if (transactionId == null) {
            transactionId = "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        }
    }

    public Transaction() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(Double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public Double getTransacAmount() {
        return transacAmount;
    }

    public void setTransacAmount(Double transacAmount) {
        this.transacAmount = transacAmount;
    }

    public String getTransacType() {
        return transacType;
    }

    public void setTransacType(String transacType) {
        this.transacType = transacType;
    }

    public Long getTransacAccountNumber() {
        return transacAccountNumber;
    }

    public void setTransacAccountNumber(Long transacAccountNumber) {
        this.transacAccountNumber = transacAccountNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
