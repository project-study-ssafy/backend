package com.ssafepole.backend.domain.user.presentation;

import com.ssafepole.backend.domain.user.service.EmailService;
import com.ssafepole.backend.domain.user.service.UserService;
import com.ssafepole.backend.global.exception.user.DuplicatedEmailException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/sign-up/send-verification-code")
    @Operation(summary = "인증 코드 전송", description = "회원가입 시 이메일로 인증 코드를 전송하는 API", tags = {"회원가입"})
    public ResponseEntity<String> sendVerificationCode(
        @RequestParam @NotBlank(message = "이메일을 입력해주세요.") @Email(message = "유효한 이메일 형식이 아닙니다.") String email) {

        // 이메일 중복 확인
        try {
            userService.validateEmail(email);
        } catch (DuplicatedEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // 인증 코드 생성 및 전송
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }
}
