package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(max = 12, message = "이름은 최대 12글자까지 가능합니다.")
    private String username;

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(max = 20, message = "별명은 20글자까지 가능합니다.")
    private String nickname;
}
