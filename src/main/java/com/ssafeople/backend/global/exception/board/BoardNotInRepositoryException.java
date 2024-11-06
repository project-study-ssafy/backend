package com.ssafeople.backend.global.exception.board;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class BoardNotInRepositoryException extends SsafeopleException {
    public static final SsafeopleException EXCEPTION = new BoardNotInRepositoryException();

    private BoardNotInRepositoryException() {
        super(ErrorCode.INVALID_BOARD_ID);
    }
}
