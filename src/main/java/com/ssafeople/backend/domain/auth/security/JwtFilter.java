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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@NonNullApi
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${spring.auth.jwt.access.header}")
    private String accessHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(accessHeader);

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.split(" ")[1];

                if (jwtUtil.isExpired(token)) {
                    log.info("Token expired for request: {}", request.getRequestURI());
                    throw ExpiredTokenException.EXCEPTION;
                }

                String email = jwtUtil.getEmail(token);
                String role = jwtUtil.getRole(token);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, null, AuthorityUtils.createAuthorityList(role));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredTokenException | InvalidTokenException e) {
                log.warn("Token validation failed for request: {}", request.getRequestURI(), e);
                throw e;
            }
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
}
