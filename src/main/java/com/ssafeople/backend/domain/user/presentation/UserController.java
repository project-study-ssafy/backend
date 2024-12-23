package com.ssafeople.backend.domain.user.presentation;

import com.ssafeople.backend.domain.auth.security.JwtUtil;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangeChattingNicknameRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordVerificationRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangeReadmeRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.EmailVerificationRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserVerifyCodeRequest;
import com.ssafeople.backend.domain.user.presentation.dto.response.MyInfoResponse;
import com.ssafeople.backend.domain.user.presentation.dto.response.UserInfoResponse;
import com.ssafeople.backend.domain.user.service.EmailService;
import com.ssafeople.backend.domain.user.service.UserService;
import com.ssafeople.backend.global.utils.user.UserUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final UserUtils userUtils;

    @Value("${spring.auth.jwt.access.header}")
    private String accessHeader;

    @PostMapping("/send-verification-code")
    public void sendVerificationCode(
        @Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {

        userService.validateEmail(emailVerificationRequest.getEmail());
        emailService.sendVerificationCode(emailVerificationRequest.getEmail());
        log.info("인증 코드 전송");
    }

    @PostMapping("/verify-code")
    public void verifyCode(
        @Valid @RequestBody UserVerifyCodeRequest verifyCodeRequest) {
        emailService.verifyEmailCode(verifyCodeRequest.getEmail(), verifyCodeRequest.getCode());
    }

    @PostMapping
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserSignUpRequest signUpRequest,
        HttpServletResponse response) {

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
    public MyInfoResponse getUser() {
        User user = userUtils.getCurrentUser();
        return new MyInfoResponse(user.getUserInfo());
    }

    @GetMapping("/{userId}")
    public UserInfoResponse getUser(@PathVariable Short userId) {
        User user = userService.getUserById(userId);
        return new UserInfoResponse(user.getUserInfo());
    }

    @PatchMapping
    public MyInfoResponse updateUserInfo(
        @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userUtils.getCurrentUser();
        userService.updateProcess(user, userUpdateRequest);
        return new MyInfoResponse(user.getUserInfo());
    }

    @DeleteMapping
    public void deleteUser() {
        User user = userUtils.getCurrentUser();
        userService.withdraw(user);
    }

    @PostMapping("/change-password")
    public MyInfoResponse changePassword(
        @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {

        emailService.isEmailVerified(changePasswordRequest.getEmail());
        User user = userService.getUser(changePasswordRequest.getEmail());
        userService.changePassword(user, changePasswordRequest);
        UserInfoVo userinfoVo = user.getUserInfo();
        return new MyInfoResponse(userinfoVo);
    }

    @PostMapping("/send-verification-code-change-password")
    public void sendVerificationCodeChangePassword(
        @Valid @RequestBody ChangePasswordVerificationRequest changePasswordVerificationRequest) {

        userService.validateEmailAndUsername(changePasswordVerificationRequest);
        emailService.sendVerificationCodeChangePassword(
            changePasswordVerificationRequest.getEmail());
    }

    @PostMapping("/readme")
    public MyInfoResponse updateReadme(
        @RequestBody ChangeReadmeRequest changeReadmeRequest) {
        User user = userUtils.getCurrentUser();
        userService.changeReadme(user, changeReadmeRequest.getReadme());

        UserInfoVo userInfo = user.getUserInfo();
        return new MyInfoResponse(userInfo);
    }

    @PostMapping("/chatting-nickname")
    public MyInfoResponse updateChattingNickname(
        @Valid @RequestBody ChangeChattingNicknameRequest changeChattingNicknameRequest) {
            User user = userUtils.getCurrentUser();
            userService.changeChattingNickname(user, changeChattingNicknameRequest.getNickname());

            return new MyInfoResponse(user.getUserInfo());
    }

}
