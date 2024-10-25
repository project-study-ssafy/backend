package com.ssafeople.backend.global.exception;

import com.ssafeople.backend.global.response.failure.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SsafeopleException extends RuntimeException {

    private ErrorCode errorCode;
}