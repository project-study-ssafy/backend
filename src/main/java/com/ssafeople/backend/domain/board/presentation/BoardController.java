package com.ssafeople.backend.domain.board.presentation;

import com.ssafeople.backend.domain.user.domain.vo.UserInfoVo;
import com.ssafeople.backend.domain.user.service.UserService;
import com.ssafeople.backend.global.exception.user.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

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
        return ResponseEntity.ok(map);
    }

}
