package com.ssafeople.backend.domain.chatting.domain.room;

import com.ssafeople.backend.domain.chatting.domain.message.ChattingMessage;
import com.ssafeople.backend.global.database.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
@Table(name = "chatting_rooms")
public class ChattingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    private String roomName;

    @OneToMany(mappedBy = "chattingRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChattingMessage> messages = new LinkedList<>();

    private Boolean isAnonymous;

    public void addMessage(ChattingMessage chattingMessage) {

        this.messages.add(chattingMessage);

        if (messages.size() > 20) {
            messages.remove(0);
        }
    }
}
