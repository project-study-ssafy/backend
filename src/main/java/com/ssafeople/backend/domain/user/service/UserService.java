package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordVerificationRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User completeSignUp(UserSignUpRequest signUpRequest);

    void validateEmail(String email);

    void validateSignUpRequest(UserSignUpRequest signUpRequest);

    User getUser(String email);

    void updateProcess(User user, UserUpdateRequest userUpdateRequest);

    void withdraw(User user);

    void validateEmailAndUsername(ChangePasswordVerificationRequest changePasswordVerificationRequest);

    void changePassword(User user, ChangePasswordRequest changePasswordRequest);

    User getUserById(Short userId);

    void changeReadme(User user, String readme);

    Long getCount();

    Page<User> findAll(Pageable pageable);
}
