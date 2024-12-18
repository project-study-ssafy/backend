package com.ssafeople.backend.global.aop;

import com.ssafeople.backend.global.exception.admin.NotAdminException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAspect {

    private final HttpSession session;

    @Before("@annotation(com.ssafeople.backend.global.annotation.AdminOnly)")
    public void checkAdminRole() {
        Object admin = session.getAttribute("admin");

        if (admin == null) {
            log.warn("관리자 권한이 없는 사용자 접근 시도");
            throw NotAdminException.EXCEPTION;
        }
    }
}
