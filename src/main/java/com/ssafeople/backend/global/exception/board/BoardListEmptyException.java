package com.ssafeople.backend.global.exception.board;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class BoardListEmptyException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new BoardListEmptyException();

    private BoardListEmptyException() {
        super(ErrorCode.INVALID_BOARD_ID);
    }
}
