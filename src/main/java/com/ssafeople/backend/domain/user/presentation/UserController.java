package com.ssafeople.backend.domain.user.presentation;

import com.ssafeople.backend.domain.auth.security.JwtUtil;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.presentation.dto.request.EmailVerificationRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserVerifyCodeRequest;
import com.ssafeople.backend.domain.user.service.EmailService;
import com.ssafeople.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Value("${spring.auth.jwt.access.header}")
    private String accessHeader;

    @PostMapping("/send-verification-code")
    @Operation(summary = "인증 코드 전송", description = "회원가입 시 이메일로 인증 코드를 전송하는 API", tags = {"회원가입"})
    public void sendVerificationCode(
        @Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {

        userService.validateEmail(emailVerificationRequest.getEmail());

        emailService.sendVerificationCode(emailVerificationRequest.getEmail());
        log.info("인증 코드 전송");
    }

    @PostMapping("/verify-code")
    @Operation(summary = "인증 코드 검증", description = "이메일로 받은 인증 코드를 검증하는 API", tags = {"회원가입"})
    public void verifyCode(
        @Valid @RequestBody UserVerifyCodeRequest verifyCodeRequest) {
        emailService.verifyEmailCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
    }


    @PostMapping
    @Operation(summary = "회원가입", description = "회원가입을 위한 API", tags = {"회원가입"})
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest,
        HttpServletResponse response) {

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

    @GetMapping
    @Operation(summary = "회원 조회", description = "회원 조회 위한 API")
    public ResponseEntity<UserInfoVo> getUser(@AuthenticationPrincipal String email) {
        UserInfoVo userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping
    @Operation(summary = "회원 정보 변경", description = "회원 정보 변경 API")
    public ResponseEntity<UserInfoVo> updateUserInfo(@AuthenticationPrincipal String email,
        @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userService.getUser(email);
        userService.updateProcess(user, userUpdateRequest);
        return ResponseEntity.ok(user.getUserInfo());
    }

    @DeleteMapping
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API")
    public void deleteUser(@AuthenticationPrincipal String email) {
        User user = userService.getUser(email);
        userService.withdraw(user);
    }

}
