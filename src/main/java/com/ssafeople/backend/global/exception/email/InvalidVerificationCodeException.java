package com.ssafeople.backend.global.exception.email;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class InvalidVerificationCodeException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new InvalidVerificationCodeException();

    private InvalidVerificationCodeException() {
        super(ErrorCode.INVALID_VERIFICATION_CODE);
    }
}