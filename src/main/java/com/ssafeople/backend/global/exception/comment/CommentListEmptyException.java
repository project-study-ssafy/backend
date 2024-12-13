package com.ssafeople.backend.global.exception.comment;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class CommentListEmptyException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new CommentListEmptyException();
    private CommentListEmptyException() {
        super(ErrorCode.EMPTY_COMMENT_LIST);
    }
}
