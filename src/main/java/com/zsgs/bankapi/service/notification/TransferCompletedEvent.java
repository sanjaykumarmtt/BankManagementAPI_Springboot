package com.zsgs.bankapi.service.notification;

import org.springframework.context.ApplicationEvent;

public class TransferCompletedEvent extends ApplicationEvent {
    private final String receiverAccountNumber;
    private final Double amount;
    private final String senderName;

    public TransferCompletedEvent(Object source, String receiverAccountNumber, Double amount, String senderName) {
        super(source);
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.senderName = senderName;
    }

    public String getReceiverAccountNumber() { return receiverAccountNumber; }
    public Double getAmount() { return amount; }
    public String getSenderName() { return senderName; }
}
