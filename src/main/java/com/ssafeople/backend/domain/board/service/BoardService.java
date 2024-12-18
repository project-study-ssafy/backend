package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.presentation.dto.response.BoardInfoResponse;

import java.util.List;

public interface BoardService {
    List<BoardInfoResponse> getAllBoards();

    Board getBoardById(Short id);

    void writeBoard(String boardName, String description);

    void deleteBoard(Short boardId);
}
