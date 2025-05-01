package com.clone.linkedin.user_service.service;

import com.clone.linkedin.user_service.clients.ConnectionsClient;
import com.clone.linkedin.user_service.dto.LoginRequestDto;
import com.clone.linkedin.user_service.dto.Person;
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
    private final ConnectionsClient connectionsClient;

    @Override
    public UserDto signUp(SignUpRequestDto signUpRequestDto) {

        boolean exists = userRepository.existsByEmail(signUpRequestDto.getEmail());
        if(exists) throw new BadRequestException("User already exists, cannot signup again.");

        User user = modelMapper.map(signUpRequestDto,User.class);
        user.setPassword(PasswordUtils.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = userRepository.save(user);

        // To Create a Person Node in GraphDB
        Person person = Person.builder()
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .build();
        connectionsClient.addPerson(person);

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
