package com.ssafeople.backend.domain.user.service;

public interface EmailService {

    void sendVerificationCode(String email);

    void verifyEmailCode(String email, String code);

    void isEmailVerified(String email);

    void deleteEmailVerificationCode(String email);

    void sendVerificationCodeChangePassword(String email);
}
