package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;

public interface UserService {

    User completeSignUp(UserSignUpRequest signUpRequest);

    void validateEmail(String email);

    void validateSignUpRequest(UserSignUpRequest signUpRequest);
}
