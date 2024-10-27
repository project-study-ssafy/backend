package com.ssafeople.backend.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafeople.backend.domain.auth.presentation.dto.CustomUserDetails;
import com.ssafeople.backend.domain.auth.presentation.dto.request.LoginRequest;
import com.ssafeople.backend.global.response.failure.ErrorResponse;
import com.ssafeople.backend.global.response.success.SuccessResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
        ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;

        this.setFilterProcessesUrl("/api/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
                LoginRequest.class);
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            log.info("email: {} passowrd: {}", email, password);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                email, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        log.info("successful authentication");

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String email = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        String token = jwtUtil.createToken(email, role, 60 * 60 * 10 * 1000L);

        jwtUtil.sendAccessToken(response, "Bearer " + token);

        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK.value(), null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(successResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed) throws IOException {

        log.info("unsuccessful authentication");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
            "로그인에 실패하였습니다.", request.getRequestURL().toString());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}