package com.ssafeople.backend.domain.chatting.presentation;

import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.chatting.service.ChattingService;
import com.ssafeople.backend.global.utils.user.UserUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatDateController {

    private final ChattingService chattingService;
    private final UserUtils userUtils;

    @GetMapping("/{roomId}")
    public List<ChattingResponse> getMessages(@PathVariable("roomId") Short roomId) {
        userUtils.getCurrentUser();
        return chattingService.getChattingRoomsMessages(roomId);
    }

}
