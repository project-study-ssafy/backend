package com.ssafepole.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class UserSignUpRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 12, message = "이름은 최대 12글자까지 가능합니다.")
    private String username;

    @Email(message = "이메일 형식으로 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소8자에서 16자까지 가능합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{8,16}$",
        message = "비밀번호는 숫자, 문자, 특수문자를 포함해야 합니다.")
    private String password;

    @Range(min = 1, max = 7, message = "1~7사이의 값을 입력해주세요.")
    private Short classNumber;

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(max = 20, message = "별명은 20글자까지 가능합니다.")
    private String nickname;

}