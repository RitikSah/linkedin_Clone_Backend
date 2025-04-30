package com.clone.linkedin.notification_service.service;

import com.clone.linkedin.notification_service.entity.Notification;
import com.clone.linkedin.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendNotificationService {

    private final NotificationRepository notificationRepository;

    public void sendNotification(Long userId, String message){
        Notification notification = Notification
                .builder()
                .userId(userId)
                .message(message)
                .build();

        notificationRepository.save(notification);
    }
}
