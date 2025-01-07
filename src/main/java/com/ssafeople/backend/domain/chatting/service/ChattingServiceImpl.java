package com.ssafeople.backend.domain.chatting.service;

import com.ssafeople.backend.domain.chatting.domain.message.ChattingMessage;
import com.ssafeople.backend.domain.chatting.domain.message.repository.ChattingMessageRepository;
import com.ssafeople.backend.domain.chatting.domain.room.ChattingRoom;
import com.ssafeople.backend.domain.chatting.domain.room.repository.ChattingRoomRepository;
import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.user.domain.User;
import com.ssafeople.backend.global.exception.chatting.NotExistChattingRoomException;
import java.util.List;
import java.util.stream.Collectors;
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
    public ChattingResponse sendMessage(Short roomId, String content, User user) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> NotExistChattingRoomException.EXCEPTION);

        String nickname;

        if (chattingRoom.getIsAnonymous()) {
            nickname = user.getChattingNickname();
        } else {
            nickname = user.getUsername();
        }

        ChattingMessage chattingMessage = ChattingMessage.builder()
            .senderId(user.getId())
            .nickname(nickname == null ? "익명": nickname)
            .content(content)
            .chattingRoom(chattingRoom)
            .build();

        chattingMessageRepository.save(chattingMessage);
        chattingRoom.addMessage(chattingMessage);

        return ChattingResponse.builder()
            .senderId(chattingMessage.getSenderId())
            .nickname(chattingMessage.getNickname())
            .content(chattingMessage.getContent())
            .createdAt(chattingMessage.getCreatedAt())
            .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChattingResponse> getChattingRoomsMessages(Short roomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId)
            .orElseThrow(() -> NotExistChattingRoomException.EXCEPTION);
        return getChattingResponses(chattingRoom);
    }

    private List<ChattingResponse> getChattingResponses(ChattingRoom chattingRoom) {
        return chattingRoom.getMessages().stream()
            .map(message -> ChattingResponse.builder()
                .nickname(message.getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .senderId(message.getSenderId())
                .build())
            .collect(Collectors.toList());
    }
}
