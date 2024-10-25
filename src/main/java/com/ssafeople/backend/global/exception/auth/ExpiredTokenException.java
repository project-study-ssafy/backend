package com.ssafeople.backend.global.exception.auth;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class ExpiredTokenException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}