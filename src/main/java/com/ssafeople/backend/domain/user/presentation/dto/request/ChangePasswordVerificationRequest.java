package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordVerificationRequest {

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    String email;

    @Size(min = 2, max = 12, message = "이름은 2 ~ 12글자까지 가능합니다.")
    private String username;

}
