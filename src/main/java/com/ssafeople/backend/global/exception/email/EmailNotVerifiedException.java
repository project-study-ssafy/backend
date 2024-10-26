package com.ssafeople.backend.global.exception.email;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import com.ssafeople.backend.global.exception.SsafeopleException;

public class EmailNotVerifiedException extends SsafeopleException {

    public static final SsafeopleException EXCEPTION = new EmailNotVerifiedException();

    private EmailNotVerifiedException() {
        super(ErrorCode.EMAIL_NOT_VERIFIED);
    }
}