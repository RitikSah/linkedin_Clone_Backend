package com.clone.linkedin.notification_service.clients;

import com.clone.linkedin.notification_service.dto.PersonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connections-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree")
    List<PersonDto> getFirstConnections();
}
