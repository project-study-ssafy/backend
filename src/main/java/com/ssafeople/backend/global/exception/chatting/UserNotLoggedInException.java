package com.ssafeople.backend.global.exception.chatting;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class UserNotLoggedInException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new UserNotLoggedInException();

    private UserNotLoggedInException() {
        super(ErrorCode.USER_NOT_LOGGED_IN);
    }

}
