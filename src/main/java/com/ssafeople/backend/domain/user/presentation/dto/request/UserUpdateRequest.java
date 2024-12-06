package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    @Size(min = 2, max = 12, message = "이름은 2 ~ 12글자까지 가능합니다.")
    private String username;

    @Size(min = 2, max = 20, message = "별명은 2~20글자까지 가능합니다.")
    private String nickname;

    @Size(max = 255, message = "한줄 소개는 255자 까지 가능합니다.")
    private String biography;
}
