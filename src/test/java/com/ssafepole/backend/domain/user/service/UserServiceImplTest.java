package com.ssafepole.backend.domain.user.service;

import com.ssafepole.backend.domain.user.domain.User;
import com.ssafepole.backend.domain.user.domain.repository.UserRepository;
import com.ssafepole.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafepole.backend.global.exception.user.DuplicatedEmailException;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional // 테스트 후 데이터 롤백
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void isEmailExists_True_WhenEmailExists() {
        // Given: 이미 존재하는 유저 생성
        User user = new User("username", "test@example.com", "passwordHash", "nickname", (short) 6);
        userRepository.save(user);

        // When & Then: validateEmail 호출 시 DuplicatedEmailException이 발생해야 함
        assertThrows(DuplicatedEmailException.class, () -> userService.validateEmail("test@example.com"));
    }

    @Test
    void validateEmail_DoesNotThrow_WhenEmailDoesNotExist() {
        // Given: 존재하지 않는 이메일
        String nonExistentEmail = "notfound@example.com";

        // When & Then: 예외가 발생하지 않아야 하며 정상적으로 반환
        assertDoesNotThrow(() -> userService.validateEmail(nonExistentEmail));
    }

    @Test
    void completeSignUp_ShouldSaveUserSuccessfully() {
        // Given: 회원가입 요청 DTO를 만듦
        UserSignUpRequest signUpRequest = new UserSignUpRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setNickname("nickname");
        signUpRequest.setClassNumber((short) 7);

        // When: 회원가입을 완료하고 유저를 저장
        User savedUser = userService.completeSignUp(signUpRequest);

        // Then: 저장된 유저가 요청과 일치하는지 확인
        assertThat(savedUser.getUsername()).isEqualTo("newUser");
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
        assertThat(savedUser.getNickname()).isEqualTo("nickname");
        assertThat(savedUser.getClassNumber()).isEqualTo((short) 7);

        // 비밀번호는 암호화된 형태로 저장되었는지 확인
        assertThat(savedUser.getPasswordHash()).isNotEqualTo("password123");
    }
}
