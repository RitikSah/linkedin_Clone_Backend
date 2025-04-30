package com.clone.linkedin.connections_service.service.impl;

import com.clone.linkedin.connections_service.auth.UserContextHolder;
import com.clone.linkedin.connections_service.entity.Person;
import com.clone.linkedin.connections_service.event.AcceptConnectionRequestEvent;
import com.clone.linkedin.connections_service.event.SendConnectionRequestEvent;
import com.clone.linkedin.connections_service.repository.PersonRepository;
import com.clone.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;

    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;


    @Override
    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        return personRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();

        log.info("Trying to send connection request, sender: {}, receiver: {}", senderId, receiverId);

        if(senderId.equals(receiverId)) throw new RuntimeException("Both senderId and receiverId is same.");

        boolean alreadySentRequest = personRepository.connectionRequestExists(senderId,receiverId);
        if(!alreadySentRequest){
            throw new RuntimeException("Connection Request already exists, cannot send again");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId,receiverId);
        if(!alreadyConnected){
                throw new RuntimeException("Already Connected users, Cannot add connection Request");
        }

        log.info("Successfully send the connection request");
        personRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendConnectionRequestEventKafkaTemplate.send("send-connection-request-topic",
                sendConnectionRequestEvent);
        return true;
    }

    @Override
    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        log.info("Trying to Accept connection request, sender: {}, receiver: {}", senderId, receiverId);

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists to accept");
        }

        log.info("Successfully Accept the connection request");
        personRepository.acceptConnectionRequest(senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptConnectionRequestEventKafkaTemplate.send("accept-connection-request-topic",
                acceptConnectionRequestEvent);

        return true;
    }

    @Override
    public Boolean rejectConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();

        boolean connectionRequestExists = personRepository.connectionRequestExists(senderId,receiverId);
        if(!connectionRequestExists){
            throw new RuntimeException("No connection request exists, Cannot Delete");
        }

       personRepository.rejectConnectionRequest(senderId, receiverId);
        return true;
    }
}
