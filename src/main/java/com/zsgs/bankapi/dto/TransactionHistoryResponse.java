package com.zsgs.bankapi.dto;

public class TransactionHistoryResponse {

    private String userName;
    private String accountNumber;
    private Long id;
    private String transactionId;
    private Double currentbalance;
    private Double transacAmount;
    private String transacType;
    private String transacAccountNumber;
    private String receiverName;
    private String currentTime;
    private String currentDate;

    public TransactionHistoryResponse(String userName, Long accountNumber, Long id, String transactionId, Double currentbalance, Double transacAmount, String transacType, Long transacAccountNumber, String receiverName, String currentTime, String currentDate) {
        this.userName = userName;
        this.accountNumber = accountNumber != null ? String.valueOf(accountNumber) : null;
        this.id = id;
        this.transactionId = transactionId;
        this.currentbalance = currentbalance;
        this.transacAmount = transacAmount;
        this.transacType = transacType;
        this.transacAccountNumber = transacAccountNumber != null ? String.valueOf(transacAccountNumber) : null;
        this.receiverName = receiverName;
        this.currentTime = currentTime;
        this.currentDate = currentDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

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

    public String getTransacAccountNumber() {
        return transacAccountNumber;
    }

    public void setTransacAccountNumber(String transacAccountNumber) {
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
