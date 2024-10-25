package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.global.exception.user.DuplicatedEmailException;
import com.ssafeople.backend.global.exception.user.DuplicatedNicknameException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User completeSignUp(UserSignUpRequest signUpRequest) {

        String passwordHash = passwordEncoder.encode(signUpRequest.getPassword());

        // User 객체 생성
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), passwordHash,
            signUpRequest.getNickname(), signUpRequest.getClassNumber());

        // 사용자 저장
        return userRepository.save(user); // 저장 후 User 객체 반환
    }

    @Override
    public void validateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw DuplicatedEmailException.EXCEPTION;
        });
    }

    @Override
    public void validateSignUpRequest(UserSignUpRequest signUpRequest) {

        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw DuplicatedEmailException.EXCEPTION;
        });

        userRepository.findByNickname(signUpRequest.getNickname()).ifPresent( user -> {
            throw DuplicatedNicknameException.EXCEPTION;
        });
    }
}
