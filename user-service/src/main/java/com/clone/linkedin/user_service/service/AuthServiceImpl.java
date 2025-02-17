package com.clone.linkedin.user_service.service;

import com.clone.linkedin.user_service.dto.LoginRequestDto;
import com.clone.linkedin.user_service.dto.SignUpRequestDto;
import com.clone.linkedin.user_service.dto.UserDto;
import com.clone.linkedin.user_service.entity.User;
import com.clone.linkedin.user_service.exception.BadRequestException;
import com.clone.linkedin.user_service.exception.ResourceNotFoundException;
import com.clone.linkedin.user_service.repository.UserRepository;
import com.clone.linkedin.user_service.utils.PasswordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {
        User user = modelMapper.map(signUpRequestDto,User.class);
        user.setPassword(PasswordUtils.hashPassword(signUpRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtils.checkPassword(loginRequestDto.getPassword(),user.getPassword());

        if(!isPasswordMatch) throw new BadRequestException("Incorrect Password");

        return jwtService.generateAccessToken(user);
    }
}
