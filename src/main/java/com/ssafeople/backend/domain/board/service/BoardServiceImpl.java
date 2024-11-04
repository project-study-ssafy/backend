package com.ssafeople.backend.domain.board.service;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.domain.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public List<Board> getAllBoards() {
        List<Board> boards = boardRepository.findAll();

        if (boards.isEmpty()) {
            return null;
        }

        return boards;
    }

    public Board getBoardById(Long id) throws Exception {
        Board board = boardRepository.findById(id);
        if (board == null) {
            throw new Exception("Board By Id is null");
        }
        return board;

    }

    public void updateBoardInfo(String newBoardName, String newDescription) {
        boardRepository.updateBoardByBoardNameAndDescription(newBoardName, newDescription);
    }
}
