package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
// import com.ssafeople.backend.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping
    public Board getBoardById(Long id) throws Exception {
        Board board = boardService.getBoardById(id);
        if (board == null) {
            throw new Exception("Invalid board id");
        }
        return board;
    }

    @GetMapping
    public void updateBoardinfo(String boardName, String newDescription) throws Exception {
        if (boardName == null) {
            throw new Exception("Invalid board name");
        }
        if (newDescription == null) {
            throw new Exception("Invalid new description");
        }
        boardService.updateBoardInfo(boardName, newDescription);
    }
}
