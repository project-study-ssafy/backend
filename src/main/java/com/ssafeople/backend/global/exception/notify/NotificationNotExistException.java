package com.ssafeople.backend.global.exception.notify;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class NotificationNotExistException extends SsafeopleException {
    public final static SsafeopleException Exception = new NotificationNotExistException();

    private NotificationNotExistException() {
        super(ErrorCode.NOT_EXIST_NOTIFY);
    }
}
