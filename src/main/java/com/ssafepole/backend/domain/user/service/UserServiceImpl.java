package com.ssafepole.backend.domain.user.service;

import com.ssafepole.backend.domain.user.domain.User;
import com.ssafepole.backend.domain.user.domain.repository.UserRepository;
import com.ssafepole.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafepole.backend.global.exception.user.DuplicatedEmailException;
import com.ssafepole.backend.global.exception.user.DuplicatedNicknameException;
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
        if (isEmailExists(email)) {
            throw new DuplicatedEmailException("이메일이 이미 사용 중입니다.");
        }
    }

    @Override
    public void validateSignUpRequest(UserSignUpRequest signUpRequest) {

        if (isEmailExists(signUpRequest.getEmail())) {
            throw new DuplicatedEmailException("이메일이 이미 사용 중입니다.");
        }

        // 별명 중복 확인
        if (isNicknameExists(signUpRequest.getNickname())) {
            throw new DuplicatedNicknameException("별명이 이미 사용 중입니다.");
        }
    }

    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isNicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
