package com.clone.linkedin.connections_service.service.impl;

import com.clone.linkedin.connections_service.auth.UserContextHolder;
import com.clone.linkedin.connections_service.entity.Person;
import com.clone.linkedin.connections_service.repository.PersonRepository;
import com.clone.linkedin.connections_service.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionsService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        return personRepository.getFirstDegreeConnections(userId);
    }
}
