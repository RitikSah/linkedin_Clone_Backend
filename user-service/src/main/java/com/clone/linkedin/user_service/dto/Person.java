package com.clone.linkedin.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {
    private Long userId;
    private String name;
}
