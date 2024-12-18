package com.ssafeople.backend.global.exception.like;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class AlreadyLikedException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new AlreadyLikedException();

    private AlreadyLikedException() {
        super(ErrorCode.LIKE_ALREADY_EXIST);
    }
}
