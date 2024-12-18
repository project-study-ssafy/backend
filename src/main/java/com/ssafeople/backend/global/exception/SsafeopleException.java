package com.ssafeople.backend.global.exception;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import lombok.Getter;

@Getter
public class SsafeopleException extends RuntimeException {

    private final ErrorCode errorCode;

    public SsafeopleException(ErrorCode errorCode) {
        super(errorCode.getReason());
        this.errorCode = errorCode;
    }
}