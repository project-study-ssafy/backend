package com.ssafeople.backend.domain.user.presentation;

import com.ssafeople.backend.domain.auth.security.JwtUtil;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserVerifyCodeRequest;
import com.ssafeople.backend.domain.user.service.EmailService;
import com.ssafeople.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Value("${spring.auth.jwt.access.header}")
    private String accessHeader;

    @PostMapping("/sign-up/send-verification-code")
    @Operation(summary = "인증 코드 전송", description = "회원가입 시 이메일로 인증 코드를 전송하는 API", tags = {"회원가입"})
    public void sendVerificationCode(
        @RequestParam @NotBlank(message = "이메일을 입력해주세요.") @Email(message = "유효한 이메일 형식이 아닙니다.") String email) {

        userService.validateEmail(email);

        emailService.sendVerificationCode(email);
        log.info("인증 코드 전송");
    }

    @PostMapping("/sign-up/verify-code")
    @Operation(summary = "인증 코드 검증", description = "이메일로 받은 인증 코드를 검증하는 API", tags = {"회원가입"})
    public void verifyCode(
        @Valid @RequestBody UserVerifyCodeRequest verifyCodeRequest) {
        emailService.verifyEmailCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
    }


    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입을 위한 API", tags = {"회원가입"})
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest, HttpServletResponse response) {

        log.info("Sign-up request received: {}", signUpRequest);

        userService.validateSignUpRequest(signUpRequest);
        emailService.isEmailVerified(signUpRequest.getEmail());

        // 사용자 정보 저장 (비밀번호 해싱 포함)
        User user = userService.completeSignUp(signUpRequest);
        // 인증 코드 삭제
        emailService.deleteEmailVerificationCode(signUpRequest.getEmail());

        String token = jwtUtil.createToken(user.getEmail(), user.getRole().getValue(),
            60 * 60 * 10 * 1000L);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setHeader(accessHeader, "Bearer " + token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
