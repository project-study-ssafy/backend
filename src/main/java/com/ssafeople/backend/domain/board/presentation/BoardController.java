package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal String email) {
        Map<String, Object> map = new HashMap<>();
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 조회", description = "특정 게시판을 조회하는 API")
    public ResponseEntity<Board> getBoardById(@PathVariable Short id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

}

