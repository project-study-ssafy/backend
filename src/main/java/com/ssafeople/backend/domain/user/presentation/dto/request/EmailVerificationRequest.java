package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerificationRequest {

    @Email(message = "유효한 이메일 형식이 아닙니다.")
    String email;
}
