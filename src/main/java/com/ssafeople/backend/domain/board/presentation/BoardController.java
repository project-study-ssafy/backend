package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.presentation.dto.response.BoardInfoResponse;
import com.ssafeople.backend.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public List<BoardInfoResponse> getBoardsList() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 조회", description = "특정 게시판을 조회하는 API")
    public Board getBoardById(@PathVariable Short id) {
        return boardService.getBoardById(id);
    }

}

