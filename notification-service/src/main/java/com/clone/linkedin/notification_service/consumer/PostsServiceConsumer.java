package com.clone.linkedin.notification_service.consumer;

import com.clone.linkedin.notification_service.clients.ConnectionsClient;
import com.clone.linkedin.notification_service.dto.PersonDto;
import com.clone.linkedin.notification_service.service.SendNotificationService;
import com.clone.linkedin.post_service.event.PostCreatedEvent;
import com.clone.linkedin.post_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {

    private final SendNotificationService sendNotificationService;
    private final ConnectionsClient connectionsClient;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("Sending notifications: handlePostCreated: {}" , postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections();

        for(PersonDto connection: connections){
            sendNotificationService.sendNotification(connection.getUserId(),
                    "Your connection "+ postCreatedEvent.getCreatorId() + " has created a post.");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        log.info("Sending notifications: handlePostLiked: {}", postLikedEvent);
        String message = String.format("Your Post, %d has been liked by %d", postLikedEvent.getPostId()
                , postLikedEvent.getLikedByUserId());

        sendNotificationService.sendNotification(postLikedEvent.getCreatorId(), message);
    }
}
