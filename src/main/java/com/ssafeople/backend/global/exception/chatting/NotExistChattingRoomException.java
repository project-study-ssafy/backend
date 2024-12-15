package com.ssafeople.backend.global.exception.chatting;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class NotExistChattingRoomException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new NotExistChattingRoomException();

    private NotExistChattingRoomException() {
        super(ErrorCode.NOT_EXIST_CHATTING_ROOM);
    }
}
