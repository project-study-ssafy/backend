package com.ssafeople.backend.global.exception.upload;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class DeleteFailedException extends SsafeopleException {
    public static SsafeopleException EXCEPTION = new DeleteFailedException();

    public DeleteFailedException() {
        super(ErrorCode.DELETE_FAILED);
    }
}
