package com.ssafeople.backend.domain.chatting.service;

import com.ssafeople.backend.domain.chatting.presentation.dto.response.ChattingResponse;
import com.ssafeople.backend.domain.user.domain.User;
import java.util.List;

public interface ChattingService {
    List<ChattingResponse> sendMessage(Short roomId, String content, User user, String userId);

}
