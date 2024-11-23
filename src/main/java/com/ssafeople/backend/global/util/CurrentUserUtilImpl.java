package com.ssafeople.backend.global.util;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserUtilImpl implements CurrentUserUtil { //

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        return userRepository.findByEmail(email).orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }

}
