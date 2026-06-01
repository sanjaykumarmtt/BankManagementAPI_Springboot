package com.zsgs.bankapi.service.notification;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.zsgs.bankapi.dto.FundTransferRequest;
import java.util.Map;

@Aspect
@Component
public class TransferNotificationAspect {
    private final ApplicationEventPublisher eventPublisher;

    public TransferNotificationAspect(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    @AfterReturning(
        pointcut = "execution(* com.zsgs.bankapi.service.transaction.transderservice.TransferService.fundTransfer(..)) && args(request)",
        returning = "result"
    )
    public void afterSuccessfulTransfer(FundTransferRequest request, Map<String, Object> result) {
        try {
            eventPublisher.publishEvent(new TransferCompletedEvent(
                this, request.getReceiverAccountNumber(), request.getAmount(), (String) result.get("SenderName")
            ));
        } catch (Exception e) {}
    }
}
