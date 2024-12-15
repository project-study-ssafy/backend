package com.ssafeople.backend.global.exception.user;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class CommentOwnerIsNotCurrentUserException extends SsafeopleException {
    public static final CommentOwnerIsNotCurrentUserException EXCEPTION = new CommentOwnerIsNotCurrentUserException();

    private CommentOwnerIsNotCurrentUserException() { super(ErrorCode.OWNER_NOW_NOTEQUAL); }
}
