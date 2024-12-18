package com.ssafeople.backend.global.exception.comment;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class CommentNotExistException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new CommentNotExistException();

    private CommentNotExistException() { super(ErrorCode.INVALID_COMMENT_ID); }
}
