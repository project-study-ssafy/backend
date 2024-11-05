package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class NotMatchEmailAndUsernameException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new NotMatchEmailAndUsernameException();

    private NotMatchEmailAndUsernameException() {
        super(ErrorCode.NOT_MATCH_EMAIL_USERNAME);
    }
}
