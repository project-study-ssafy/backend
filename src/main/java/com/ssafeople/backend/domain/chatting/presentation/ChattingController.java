package com.ssafeople.backend.domain.chatting.presentation;

import com.ssafeople.backend.domain.chatting.presentation.dto.request.ChattingRequest;
import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.chatting.service.ChattingService;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.utils.user.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ChattingController {

    private final SimpMessagingTemplate template;
    private final ChattingService chattingService;
    private final UserUtils userUtils;

    @MessageMapping("/send/{roomId}")
    public void sendToRoom(@RequestBody ChattingRequest chatMessageDto, @DestinationVariable Short roomId) {
        log.info("Room {} Message: {}", roomId, chatMessageDto.getContent());

        User user = userUtils.getCurrentUser();

        ChattingResponse response = chattingService.sendMessage(
            roomId, chatMessageDto.getContent(), user);

        template.convertAndSend("/sub/chat/room/" + roomId, response);
    }
}