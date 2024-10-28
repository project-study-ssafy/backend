package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;

public interface UserService {

    User completeSignUp(UserSignUpRequest signUpRequest);

    void validateEmail(String email);

    void validateSignUpRequest(UserSignUpRequest signUpRequest);

    UserInfoVo getUserInfo(String email);

    User getUser(String email);

    void updateProcess(User user, UserUpdateRequest userUpdateRequest);

    void withdraw(User user);
}
