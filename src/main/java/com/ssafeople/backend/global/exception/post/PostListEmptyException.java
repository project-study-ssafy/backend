package com.ssafeople.backend.global.exception.post;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class PostListEmptyException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new PostListEmptyException();

    private PostListEmptyException() {
        super(ErrorCode.EMPTY_POST_LIST);
    }
}
