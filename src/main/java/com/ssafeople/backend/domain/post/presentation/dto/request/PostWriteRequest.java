package com.ssafeople.backend.domain.post.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostWriteRequest {

    @NotBlank(message = "제목은 필수 입력 항목입나다.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
