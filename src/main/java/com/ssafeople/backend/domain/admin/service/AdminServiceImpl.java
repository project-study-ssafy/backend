package com.ssafeople.backend.domain.admin.service;

import com.ssafeople.backend.domain.user.domain.Role;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.admin.AdminLoginFailedException;
import com.ssafeople.backend.global.exception.admin.NotAdminException;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserNotFoundException.EXCEPTION);

        if (user.getRole() != Role.ADMIN) {
            throw NotAdminException.EXCEPTION;
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw AdminLoginFailedException.EXCEPTION;
        }
        return user;
    }

}
