package com.ssafeople.backend.domain.chatting.domain.message.repository;

import com.ssafeople.backend.domain.chatting.domain.message.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

}
