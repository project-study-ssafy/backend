package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
// import com.ssafeople.backend.domain.post.domain.Post;
import io.swagger.v3.oas.annotations.Operation;
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

    @GetMapping("/")
    @Operation(summary = "게시판 목록 조회", description = "전체 게시판의 목록 반환하는 API")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 조회", description = "특정 게시판을 조회하는 API")
    public Board getBoardById(@PathVariable Short id) {
        return boardService.getBoardById(id);
    }

}
