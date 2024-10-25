package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class DuplicatedEmailException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new DuplicatedEmailException();

    private DuplicatedEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}