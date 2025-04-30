package com.clone.linkedin.notification_service.consumer;

import com.clone.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.clone.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.clone.linkedin.notification_service.service.SendNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotificationService sendNotificationService;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent){
        String message = "You have received a connection request from user with id: %d"+
                sendConnectionRequestEvent.getSenderId();

        sendNotificationService.sendNotification(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent){
        String message = "Your connection request has been accepted by the user with id: %d"+
                acceptConnectionRequestEvent.getReceiverId();

        sendNotificationService.sendNotification(acceptConnectionRequestEvent.getSenderId(), message);
    }
}
