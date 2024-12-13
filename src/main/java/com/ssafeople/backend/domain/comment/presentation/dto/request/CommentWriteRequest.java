package com.ssafeople.backend.domain.comment.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentWriteRequest {

    @NotBlank
    private String content;

}
