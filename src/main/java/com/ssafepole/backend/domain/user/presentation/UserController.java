package com.ssafepole.backend.domain.user.presentation;

import com.ssafepole.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafepole.backend.domain.user.service.EmailService;
import com.ssafepole.backend.domain.user.service.UserService;
import com.ssafepole.backend.global.exception.user.DuplicatedEmailException;
import com.ssafepole.backend.global.exception.user.DuplicatedNicknameException;
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

    @PostMapping("/sign-up/verify-code")
    @Operation(summary = "인증 코드 검증", description = "이메일로 받은 인증 코드를 검증하는 API", tags = {"회원가입"})
    public ResponseEntity<String> verifyCode(@RequestParam String email,
        @RequestParam String code) {

        boolean isVerified = emailService.verifyEmailCode(email, code);

        if (!isVerified) {
            return ResponseEntity.badRequest().body("잘못된 인증 코드입니다.");
        }
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }


    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입을 위한 API", tags = {"회원가입"})
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest) {

        try {
            userService.validateSignUpRequest(signUpRequest);
        } catch (DuplicatedEmailException |DuplicatedNicknameException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // 이메일 인증 확인
        if (!emailService.isEmailVerified(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body("이메일 인증이 필요합니다.");
        }

        // 사용자 정보 저장 (비밀번호 해싱 포함)
        userService.completeSignUp(signUpRequest);

        // 인증 코드 삭제
        emailService.deleteEmailVerificationCode(signUpRequest.getEmail());

        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
}
