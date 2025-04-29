package com.clone.linkedin.connections_service.service;

import com.clone.linkedin.connections_service.entity.Person;

import java.util.List;

public interface ConnectionsService {
    List<Person> getFirstDegreeConnections(Long userId);
}
