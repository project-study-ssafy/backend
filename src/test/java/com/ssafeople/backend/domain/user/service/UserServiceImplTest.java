package com.ssafeople.backend.domain.user.service;

import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.domain.user.domain.repository.UserRepository;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.ChangePasswordVerificationRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserSignUpRequest;
import com.ssafeople.backend.domain.user.presentation.dto.request.UserUpdateRequest;
import com.ssafeople.backend.global.exception.user.DuplicatedEmailException;
import com.ssafeople.backend.global.exception.user.DuplicatedNicknameException;
import com.ssafeople.backend.global.exception.user.NotMatchEmailAndUsernameException;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("중복 이메일 확인 테스트")
    void isEmailExists_True_WhenEmailExists() {
        // Given: 이미 존재하는 유저 생성
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When & Then: validateEmail 호출 시 DuplicatedEmailException이 발생해야 함
        assertThrows(DuplicatedEmailException.class,
            () -> userService.validateEmail("test@example.com"));
    }

    @Test
    @DisplayName("정상 이메일 확인 테스트")
    void validateEmail_DoesNotThrow_WhenEmailDoesNotExist() {
        // Given: 존재하지 않는 이메일
        String nonExistentEmail = "notfound@example.com";

        // When & Then: 예외가 발생하지 않아야 하며 정상적으로 반환
        assertDoesNotThrow(() -> userService.validateEmail(nonExistentEmail));
    }

    @Test
    @DisplayName("중복 별명 확인 테스트")
    void isNicknameExists_True_WhenNicknameExists() {
        // Given: 이미 존재하는 유저 생성
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When: 중복된 별명으로 회원가입
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest();
        userSignUpRequest.setUsername("username");
        userSignUpRequest.setEmail("test2@example.com");
        userSignUpRequest.setPassword("passwordHash");
        userSignUpRequest.setNickname("nickname");

        // When & Then: 중복된 별명으로 회원가입시 예외 발생
        assertThrows(DuplicatedNicknameException.class,
            () -> userService.validateSignUpRequest(userSignUpRequest));

    }

    @Test
    @DisplayName("정상적인 회원 가입 테스트")
    void completeSignUp_ShouldSaveUserSuccessfully() {
        // Given: 회원가입 요청 DTO를 만듦
        UserSignUpRequest signUpRequest = new UserSignUpRequest();
        signUpRequest.setUsername("newUser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setNickname("nickname");

        // When: 회원가입을 완료하고 유저를 저장
        User savedUser = userService.completeSignUp(signUpRequest);

        // Then: 저장된 유저가 요청과 일치하는지 확인
        assertThat(savedUser.getUsername()).isEqualTo("newUser");
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
        assertThat(savedUser.getNickname()).isEqualTo("nickname");

        // 비밀번호는 암호화된 형태로 저장되었는지 확인
        assertThat(savedUser.getPasswordHash()).isNotEqualTo("password123");
    }

    @Test
    @DisplayName("사용자 조회 기능")
    void retrieveUser_Success() {

        // Given: 사용자가 저장되어 있을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When: 사용자를 조회하면
        UserInfoVo userInfo = userService.getUserInfo("test@example.com");

        // Then: 저장된 유저가 저장된 정보와 일치하는지 확인
        assertThat(userInfo.getNickname()).isEqualTo("nickname");
        assertThat(userInfo.getEmail()).isEqualTo("test@example.com");
        assertThat(userInfo.getUsername()).isEqualTo("username");
    }

    @Test
    @DisplayName("사용자 조회 실패")
    void retrieveUser_Fail_WhenUserDoesNotExist() {
        // Given: 사용자를 저장했을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When & Then 존재하지 않는 이메일르 조회하면 예외가 발생
        assertThrows(UserNotFoundException.class,
            () -> userService.getUserInfo("test2@example.com"));
    }

    @Test
    @DisplayName("사용자 업데이트 성공")
    void updateUser_WhenValidData_Success() {
        // Given: 사용자를 저장했을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When: 업데이트 요청이 왔을 때
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUsername("테스터");
        userUpdateRequest.setNickname("별명");

        // Then: 유저의 정보가 변경이 됬는지 확인
        userService.updateProcess(user, userUpdateRequest);
        assertThat(user.getUsername()).isEqualTo("테스터");
        assertThat(user.getNickname()).isEqualTo("별명");
    }

    @Test
    @DisplayName("사용자 업데이트 실패(중복된 별명)")
    void updateUser_WhenInvalidData_Fail() {
        // Given: 두 명의 사용자를 저장했을 때
        User user1 = new User("username", "test@example.com", "passwordHash", "nickname");
        User user2 = new User("username", "test2@example.com", "passwordHash", "nickname2");
        userRepository.save(user1);
        userRepository.save(user2);

        // When: 중복된 별명으로 요청이 왔을 때
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUsername("테스터");
        userUpdateRequest.setNickname("nickname");

        // Then: 정보를 변경할 수 없는 예외 발생
        assertThrows(DuplicatedNicknameException.class,
            () -> userService.updateProcess(user2, userUpdateRequest));
    }

    @Test
    @DisplayName("사용자 업데이트 성공 (별명의 변경이 없는 경우)")
    void updateUser_WithUnchangedNickname_Success() {
        // Given: 사용자를 저장
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When: 동일한 별명으로 변경 요청이 왔을 대
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUsername("name");
        userUpdateRequest.setNickname("nickname");

        // Then: 성공적으로 변경
        userService.updateProcess(user, userUpdateRequest);
        assertThat(user.getUsername()).isEqualTo("name");
        assertThat(user.getNickname()).isEqualTo("nickname");
    }

    @Test
    @DisplayName("사용자 탈퇴 성공")
    void withdrawUser_Success() {
        // Given: 사용자를 저장했을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // When: 사용자가 탈퇴하면
        userService.withdraw(user);

        // Then: 사용자가 조회되지 않는다.
        assertThrows(UserNotFoundException.class, () -> userService.getUser("test@example.com"));
    }

    @Test
    @DisplayName("사용자 이름 이메일 확인 성공")
    void checkEmailAndUsername_Success() {

        // given: 사용자가 있을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // when: 동일한 이메일과 이름으로 전송을 하면
        ChangePasswordVerificationRequest changePasswordVerificationRequest = new ChangePasswordVerificationRequest();
        changePasswordVerificationRequest.setEmail(user.getEmail());
        changePasswordVerificationRequest.setUsername(user.getUsername());

        // then
        assertDoesNotThrow(() -> userService.validateEmailAndUsername(changePasswordVerificationRequest));

    }

    @Test
    @DisplayName("사용자 이름 이메일 확인 실패")
    void checkEmailAndUsername_Fail() {

        // given: 사용자가 있을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // when: 동일한 이메일과 다른 이름 으로 전송을 하면
        ChangePasswordVerificationRequest changePasswordVerificationRequest = new ChangePasswordVerificationRequest();
        changePasswordVerificationRequest.setEmail(user.getEmail());
        changePasswordVerificationRequest.setUsername("다른 이름");

        // then
        assertThrows(NotMatchEmailAndUsernameException.class, () -> userService.validateEmailAndUsername(changePasswordVerificationRequest));
    }

    @Test
    @DisplayName("비밀 번호 변공 성공")
    void changePassword_Success() {

        // given: 사용자가 있을 때
        User user = new User("username", "test@example.com", "passwordHash", "nickname");
        userRepository.save(user);

        // when: 비밀번호 변경
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail(user.getEmail());
        changePasswordRequest.setPassword("newPasswordHash");
        userService.changePassword(changePasswordRequest);

        // then: 변경이 됨
        assertThat(user.getPasswordHash()).isNotEqualTo("passwordHash");
    }

}
