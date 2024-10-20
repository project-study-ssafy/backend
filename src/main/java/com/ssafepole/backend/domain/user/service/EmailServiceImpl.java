package com.ssafepole.backend.domain.user.service;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")  // 발신자 이메일 주소를 주입받습니다.
    private String fromEmail;

    private static final int CODE_LENGTH = 6;
    private static final String NUMERIC_CHARACTERS = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ConcurrentHashMap<String, String> verificationCodes = new ConcurrentHashMap<>(); // 임시 저장소
    private final ConcurrentHashMap<String, Boolean> emailVerificationStatus = new ConcurrentHashMap<>(); // 이메일 인증 상태 저장

    @Override
    public void sendVerificationCode(String email) {
        // 인증 코드 생성 로직 (랜덤 코드 생성 등)
        String code = generateVerificationCode();
        verificationCodes.put(email, code);

        // 이메일 내용 설정
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("회원가입 인증 코드");
        message.setText("인증 코드는 " + code + "입니다.");

        // 이메일 전송
        mailSender.send(message);
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        String storedCode = verificationCodes.get(email); // 임시 저장소에서 코드 가져오기
        boolean isVerified = storedCode != null && storedCode.equals(code);

        log.info("이메일: {}", email);
        log.info("인증 코드: {}, 저장된 인증 코드: {}", code, storedCode);

        if (isVerified) {
            emailVerificationStatus.put(email, true);
            verificationCodes.remove(email); // 인증 후 코드 삭제
        }

        return isVerified;
    }

    @Override
    public boolean isEmailVerified(String email) {
        return emailVerificationStatus.getOrDefault(email, false);
    }

    @Override
    public void deleteEmailVerificationCode(String email) {
        emailVerificationStatus.remove(email);
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(NUMERIC_CHARACTERS.length());
            code.append(NUMERIC_CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }
}
