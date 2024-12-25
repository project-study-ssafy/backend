package com.ssafeople.backend.domain.user.presentation.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeChattingNicknameRequest {

    @Size(min = 2, max = 12, message = "별명은 2 ~ 12글자까지 가능합니다.")
    private String nickname;

}
