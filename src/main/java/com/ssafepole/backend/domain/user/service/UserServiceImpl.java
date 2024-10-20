package com.ssafepole.backend.domain.user.service;

public class UserServiceImpl {
import com.ssafepole.backend.domain.user.domain.repository.UserRepository;
import com.ssafepole.backend.global.exception.user.DuplicatedEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public void validateEmail(String email) {
        if (isEmailExists(email)) {
            throw new DuplicatedEmailException("이메일이 이미 사용 중입니다.");
        }
    }

    private boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
