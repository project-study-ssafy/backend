package com.ssafeople.backend.domain.user.presentation.dto.response;

import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {

    private Short id;
    private String username;
    private String email;
    private String nickname;
    private String role;
    private String readme;
    private String biography;

    public UserInfoResponse(UserInfoVo userInfoVo) {
        this.id = userInfoVo.getId();
        this.username = userInfoVo.getUsername();
        this.email = userInfoVo.getEmail();
        this.nickname = userInfoVo.getNickname();
        this.role = userInfoVo.getRole();
        this.readme = userInfoVo.getReadme();
        this.biography = userInfoVo.getBiography();
    }

}
