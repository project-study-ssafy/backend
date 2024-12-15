package com.ssafeople.backend.domain.chatting.domain.room.repository;

import com.ssafeople.backend.domain.chatting.domain.room.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Short> {

}
