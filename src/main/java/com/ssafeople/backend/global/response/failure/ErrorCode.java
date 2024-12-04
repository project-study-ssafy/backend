package com.ssafeople.backend.global.response.failure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_VERIFICATION_CODE(400, "유효하지 않은 인증번호입니다."),
    EMAIL_NOT_VERIFIED(400, "인증되지 않은 이메일입니다."),
    NOT_MATCH_EMAIL_USERNAME(400, "사용자의 이메일과 이름이 일치하지 않습니다."),

    INVALID_TOKEN(401, "토큰이 유효하지 않습니다."),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다."),

    URL_INPUT_ERROR(404, "잘못된 URL 입니다"),
    USER_NOT_FOUND(404,"사용자가 존재하지 않습니다"),
    METHOD_NOT_ALLOWED(405, "http 메소드가 잘못되었습니다."),

    DUPLICATE_EMAIL(422, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(422, "이미 존재하는 닉네입입니다."),

    INTERNAL_SERVER_ERROR(500, "내부 서버 에러"),

    INVALID_BOARD_ID(404, "유효하지 않은 게시판 접근"),
    EMPTY_BOARD_LIST(400, "게시판 목록이 존재하지 않음"),

    INVALID_POST_ID(404, "현재 접근하려는 게시판이 존재하지 않습니다."),
    EMPTY_POST_LIST(400, "게시글 목록이 존재하지 않음"),

    OWNER_NOTEQUAL_NOW(403, "해당 게시글에 대한 권한이 없습니다.");

    private final int status;
    private final String reason;
}