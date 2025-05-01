package com.clone.linkedin.user_service.clients;

import com.clone.linkedin.user_service.dto.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @PostMapping("/core/addPerson")
    public Person addPerson(Person person);
}
