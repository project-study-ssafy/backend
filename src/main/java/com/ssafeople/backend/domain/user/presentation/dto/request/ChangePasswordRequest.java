package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,16}$",
        message = "비밀번호는 숫자, 문자, 특수문자를 포함해야 합니다.")
    private String password;

}
