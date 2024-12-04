package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class PostOwnerIsNotCurrentUserException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new PostOwnerIsNotCurrentUserException();
    private PostOwnerIsNotCurrentUserException() {
        super(ErrorCode.OWNER_NOTEQUAL_NOW);
    }
}
