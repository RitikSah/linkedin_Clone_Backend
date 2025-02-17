package com.clone.linkedin.user_service.service;

import com.clone.linkedin.user_service.dto.LoginRequestDto;
import com.clone.linkedin.user_service.dto.SignUpRequestDto;
import com.clone.linkedin.user_service.dto.UserDto;

public interface AuthService {
    UserDto signUp(SignUpRequestDto signUpRequestDto);

    String login(LoginRequestDto loginRequestDto);
}
