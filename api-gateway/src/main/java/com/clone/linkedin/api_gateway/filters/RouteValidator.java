package com.clone.linkedin.api_gateway.filters;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    /*
     * in this method we tell that which api end points should not need to authenticate it is open for everyone
     */
    public static final List<String> openApiEndpoints = List.of(
            "connections/core/addPerson"
    );

    /*
     * return true or false , true if uri is secured or not otherwise else
     */
    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
