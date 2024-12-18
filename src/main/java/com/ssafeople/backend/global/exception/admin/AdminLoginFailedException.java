package com.ssafeople.backend.global.exception.admin;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class AdminLoginFailedException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new AdminLoginFailedException();

    private AdminLoginFailedException() {
        super(ErrorCode.ADMIN_LOGIN_FAILED);
    }

}
