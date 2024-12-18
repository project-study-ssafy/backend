package com.ssafeople.backend.global.exception.admin;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class NotAdminException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new NotAdminException();

    private NotAdminException() {
        super(ErrorCode.NOT_ADMIN);
    }

}
