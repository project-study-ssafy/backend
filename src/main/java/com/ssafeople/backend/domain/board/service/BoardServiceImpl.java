package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
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
            return null;
        }

        return boards;
    }

    @Transactional(readOnly = true)
    public Board getBoardById(Short id) throws Exception {
        return boardRepository.findById(id).orElseThrow(() -> new Exception("Board By Id is null"));
    }

    @Transactional
    public void updateBoardInfo(String newBoardName, String newDescription) {
        boardRepository.updateBoardByBoardNameAndDescription(newBoardName, newDescription);
    }
}
