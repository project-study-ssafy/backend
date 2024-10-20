package com.ssafepole.backend.global.exception.user;

public class DuplicatedNicknameException extends RuntimeException {

    public DuplicatedNicknameException(String message) {
        super(message);
    }
}