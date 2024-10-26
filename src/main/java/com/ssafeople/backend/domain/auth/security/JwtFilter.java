package com.ssafeople.backend.domain.auth.security;

import com.ssafeople.backend.global.exception.auth.ExpiredTokenException;
import com.ssafeople.backend.global.exception.auth.InvalidTokenException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@NonNullApi
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${spring.auth.jwt.access.header}")
    private String accessHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String authorization = request.getHeader(accessHeader);

        // 인증이 필수인 경로일 경우, 토큰이 없거나 형식이 잘못된 경우 예외 발생
        if (requestURI.startsWith("/api/v1/auth/")) {
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                log.info("Token is null or invalid for protected path: {}", requestURI);
                throw InvalidTokenException.EXCEPTION;
            }
        } else {
            // 인증이 필수 아닌 경로에서 토큰이 없으면 다음 필터로 넘어감
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 유효한 토큰이 있는 경우에만 검증 로직 수행
        try {
            String token = authorization.split(" ")[1];

            if (jwtUtil.isExpired(token)) {
                log.info("Token expired for request: {}", requestURI);
                throw ExpiredTokenException.EXCEPTION;
            }

            String email = jwtUtil.getEmail(token);
            String role = jwtUtil.getRole(token);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email, null, AuthorityUtils.createAuthorityList(role));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (ExpiredTokenException | InvalidTokenException e) {
            log.warn("Token validation failed for request: {}", requestURI, e);
            throw e;  // 예외 발생 시 요청이 중단됨
        }
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}