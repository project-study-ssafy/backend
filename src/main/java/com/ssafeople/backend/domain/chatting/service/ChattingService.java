package com.ssafeople.backend.domain.chatting.service;

import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.user.domain.User;
import java.util.List;

public interface ChattingService {
    ChattingResponse sendMessage(Short roomId, String content, User user, String userId);

    List<ChattingResponse> getChattingRoomsMessages(Short roomId, User user);
}
