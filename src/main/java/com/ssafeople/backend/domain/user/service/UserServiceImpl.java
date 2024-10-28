package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;
import com.ssafeople.backend.global.exception.user.DuplicatedEmailException;
import com.ssafeople.backend.global.exception.user.DuplicatedNicknameException;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
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
            signUpRequest.getNickname());

        // 사용자 저장
        return userRepository.save(user); // 저장 후 User 객체 반환
    }

    @Override
    @Transactional(readOnly = true)
    public void validateEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw DuplicatedEmailException.EXCEPTION;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public void validateSignUpRequest(UserSignUpRequest signUpRequest) {

        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw DuplicatedEmailException.EXCEPTION;
        });

        userRepository.findByNickname(signUpRequest.getNickname()).ifPresent(user -> {
            throw DuplicatedNicknameException.EXCEPTION;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoVo getUserInfo(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> UserNotFoundException.EXCEPTION).getUserInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

    @Override
    public void updateProcess(User user, UserUpdateRequest userUpdateRequest) {
        // 사용자의 원래 닉네임을 가져옵니다.
        String currentNickname = user.getNickname();

        // 새로운 닉네임이 원래 닉네임과 다르면서 다른 사용자에게 이미 존재하는지 확인합니다.
        if (!userUpdateRequest.getNickname().equals(currentNickname)) {
            userRepository.findByNickname(userUpdateRequest.getNickname()).ifPresent(u -> {
                throw DuplicatedNicknameException.EXCEPTION;
            });
        }

        // 사용자 정보 업데이트
        user.update(userUpdateRequest.getUsername(), userUpdateRequest.getNickname());
    }

    @Override
    public void withdraw(User user) {
        userRepository.delete(user);
    }

}
