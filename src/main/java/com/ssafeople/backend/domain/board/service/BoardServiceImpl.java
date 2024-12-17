package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import com.ssafeople.backend.global.exception.board.BoardListEmptyException;
import com.ssafeople.backend.global.exception.board.BoardNotInRepositoryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<Board> getAllBoards() {
        List<Board> boards = boardRepository.findAll();

        if (boards.isEmpty()) {
            throw BoardListEmptyException.EXCEPTION;
        }

        return boards;
    }

    @Transactional(readOnly = true)
    public Board getBoardById(Short id) {
        return boardRepository.findById(id).orElseThrow(() -> BoardNotInRepositoryException.EXCEPTION);
    }

    @Override
    public void writeBoard(String boardName, String description) {
        Board board = new Board(boardName, description);
        boardRepository.save(board);
    }

    @Override
    public void deleteBoard(Short boardId) {
        boardRepository.deleteById(boardId);
    }


}
