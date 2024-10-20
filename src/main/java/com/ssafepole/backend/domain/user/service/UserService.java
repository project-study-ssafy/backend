package com.ssafepole.backend.domain.user.service;

import com.ssafepole.backend.domain.user.domain.User;
import com.ssafepole.backend.domain.user.presentation.dto.request.UserSignUpRequest;

public interface UserService {

    User completeSignUp(UserSignUpRequest signUpRequest);

    void validateEmail(String email);

    void validateSignUpRequest(UserSignUpRequest signUpRequest);
}
