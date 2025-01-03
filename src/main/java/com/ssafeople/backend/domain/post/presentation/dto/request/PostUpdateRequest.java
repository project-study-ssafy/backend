package com.ssafeople.backend.domain.post.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequest {
    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    @NotBlank(message = "내용은 빈 칸일 수 없습니다. 내용을 입력해주세요.")
    private String content;

    private List<MultipartFile> images;
}
