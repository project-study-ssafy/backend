package com.ssafeople.backend.domain.user.domain.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoVo {
    private final Short id;
    private final String username;
    private final String email;
    private final String nickname;
    private final String role;
}
