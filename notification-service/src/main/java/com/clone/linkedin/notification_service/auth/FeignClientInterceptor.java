package com.clone.linkedin.notification_service.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = UserContextHolder.getCurrentUserId();
        if(userId != null){
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }
}
