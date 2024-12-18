package com.ssafeople.backend.global.exception.like;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class LikeNotExistsException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new LikeNotExistsException();

    private LikeNotExistsException() {
        super(ErrorCode.INVALID_LIKE);
    }
}
