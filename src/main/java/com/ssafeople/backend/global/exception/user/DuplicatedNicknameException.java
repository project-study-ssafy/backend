package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class DuplicatedNicknameException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new DuplicatedNicknameException();

    private DuplicatedNicknameException() {
        super(ErrorCode.DUPLICATE_NICKNAME);
    }
}