package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.board.domain.Board;
import com.ssafeople.backend.domain.board.service.BoardService;
import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.service.UserService;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> home(@AuthenticationPrincipal String email) {
        Map<String, Object> map = new HashMap<>();

        try {
            UserInfoVo userInfo = userService.getUserInfo(email);
            map.put("user", userInfo);
        } catch (UserNotFoundException e) {
            log.info("로그인 하지 않은 사용자");
        }

        try {
            List<Board> boards = boardService.getAllBoards();
            map.put("boards", boards);
        } catch (Exception e) {
            log.info("게시판 비어있음");
        }

        return ResponseEntity.ok(map);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시판 조회", description = "특정 게시판을 조회하는 API")
    public ResponseEntity<Board> getBoardById(@PathVariable Short id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

}

