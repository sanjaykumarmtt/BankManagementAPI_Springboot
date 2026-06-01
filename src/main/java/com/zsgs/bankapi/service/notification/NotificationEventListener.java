package com.zsgs.bankapi.service.notification;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleTransfer(TransferCompletedEvent event) {
        try {
            String destination = "/topic/notifications/" + event.getReceiverAccountNumber();
            String message = String.format("You received $%.2f from %s", event.getAmount(), event.getSenderName());
            messagingTemplate.convertAndSend(destination, new NotificationMessage(message));
        } catch (Exception e) {}
    }

    static class NotificationMessage {
        private String message;
        public NotificationMessage(String message) { this.message = message; }
        public String getMessage() { return message; }
    }
}
