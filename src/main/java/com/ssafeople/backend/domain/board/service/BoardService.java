package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getAllBoards();

    Board getBoardById(Long id);
}
