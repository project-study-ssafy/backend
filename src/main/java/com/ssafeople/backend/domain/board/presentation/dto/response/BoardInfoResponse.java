package com.ssafeople.backend.domain.board.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class BoardInfoResponse {
    private Short boardId;
    private String boardName;
    private String boardDescription;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
