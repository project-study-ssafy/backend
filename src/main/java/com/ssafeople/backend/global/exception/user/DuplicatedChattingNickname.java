package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class DuplicatedChattingNickname extends SsafeopleException {

    public static SsafeopleException EXCEPTION = new DuplicatedChattingNickname();

    private DuplicatedChattingNickname() {
        super(ErrorCode.DUPLICATE_CHATTING_NICKNAME);
    }

}
