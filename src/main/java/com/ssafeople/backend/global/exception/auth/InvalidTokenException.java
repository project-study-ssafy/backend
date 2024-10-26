package com.ssafeople.backend.global.exception.auth;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class InvalidTokenException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}