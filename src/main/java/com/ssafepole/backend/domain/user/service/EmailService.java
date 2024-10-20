package com.ssafepole.backend.domain.user.service;

public interface EmailService {

    void sendVerificationCode(String email);

    boolean verifyEmailCode(String email, String code);

    boolean isEmailVerified(String email);

    void deleteEmailVerificationCode(String email);
}
