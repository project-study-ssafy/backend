package com.ssafeople.backend.domain.chatting.service;

import com.ssafeople.backend.domain.chatting.domain.message.ChattingMessage;
import com.ssafeople.backend.domain.chatting.domain.message.repository.ChattingMessageRepository;
import com.ssafeople.backend.domain.chatting.domain.room.ChattingRoom;
import com.ssafeople.backend.domain.chatting.domain.room.repository.ChattingRoomRepository;
import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.chatting.NotExistChattingRoomException;
import com.ssafeople.backend.global.exception.chatting.UserNotLoggedInException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChattingServiceImpl implements ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;

    private final ChattingRoomRepository chattingRoomRepository;

    @Override
    public ChattingResponse sendMessage(Short roomId, String content, User user, String sessionId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> NotExistChattingRoomException.EXCEPTION);

        if (!chattingRoom.getIsAnonymous() && user == null) {
            throw UserNotLoggedInException.EXCEPTION;
        }

        String nickname;

        if (chattingRoom.getIsAnonymous()) {
            nickname = "익명";
        } else {
            nickname = user.getNickname();
        }

        ChattingMessage chattingMessage = ChattingMessage.builder()
            .sessionId(sessionId)
            .nickname(nickname)
            .content(content)
            .chattingRoom(chattingRoom)
            .build();

        chattingMessageRepository.save(chattingMessage);
        chattingRoom.addMessage(chattingMessage);

        return ChattingResponse.builder()
            .sessionId(chattingMessage.getSessionId())
            .nickname(chattingMessage.getNickname())
            .content(chattingMessage.getContent())
            .createdAt(chattingMessage.getCreatedAt())
            .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChattingResponse> getChattingRoomsMessages(Short roomId, User user) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> NotExistChattingRoomException.EXCEPTION);

        if (!chattingRoom.getIsAnonymous() && user == null) {
            throw UserNotLoggedInException.EXCEPTION;
        }
        return getChattingResponses(chattingRoom);
    }

    private List<ChattingResponse> getChattingResponses(ChattingRoom chattingRoom) {
        List<ChattingResponse> responses = new ArrayList<>();

        for (ChattingMessage message : chattingRoom.getMessages()) {
            ChattingResponse response =
                ChattingResponse.builder()
                    .nickname(message.getNickname())
                    .content(message.getContent())
                    .createdAt(message.getCreatedAt())
                    .sessionId(message.getSessionId())
                    .build();
            responses.add(response);
        }
        return responses;
    }

}
